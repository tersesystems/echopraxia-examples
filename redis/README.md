# Redis Condition Store

Scenario: you want to change logging dynamically, without restarting the application.

Solution: Use conditions and connect to a key/value backend like Redis to process scripts.

## Implementation

The implementation uses a filter, which connects to Redis to query for the level and caches the result using [Caffeine](https://github.com/ben-manes/caffeine).

```java
public class JedisFilter implements CoreLoggerFilter, AutoCloseable {

  private final JedisPooled client;
  private static final Logger logger = org.slf4j.LoggerFactory.getLogger(JedisFilter.class);

  private final LoadingCache<String, Condition> cache;

  public JedisFilter() {
    HostAndPort config = new HostAndPort(Protocol.DEFAULT_HOST, 6379);
    PooledConnectionProvider provider = new PooledConnectionProvider(config);
    client = new JedisPooled(provider);

    // Set up cache and refresh
    cache =
            Caffeine.newBuilder()
                    .maximumSize(10_000)
                    .refreshAfterWrite(Duration.ofSeconds(1)) // refresh after every cache access
                    .build(this::queryRedis);

    // Load up a starting set of keys from cache
    Set<String> keys = getKeysFromRedis();
    cache.getAll(keys);
  }

  private Set<String> getKeysFromRedis() {
    ScanParams scanParams = new ScanParams().count(1000);
    Set<String> allKeys = new HashSet<>();
    String cur = ScanParams.SCAN_POINTER_START;
    do {
      ScanResult<String> scanResult = client.scan(cur, scanParams);
      allKeys.addAll(scanResult.getResult());
      cur = scanResult.getCursor();
      if (allKeys.size() >= 1000) break;
    } while (!cur.equals(ScanParams.SCAN_POINTER_START));

    return allKeys;
  }

  private Condition queryRedis(String key) {
    String script = client.get(key);
    return script != null ? ScriptCondition.create(false, script, e -> logger.error("Cannot compile script!", e)) : null;
  }

  public void close() {
    client.close();
  }

  @Override
  public CoreLogger apply(CoreLogger coreLogger) {
    Condition condition = new JedisCondition(coreLogger.getName());
    return coreLogger.withCondition(condition);
  }

  class JedisCondition implements Condition {
    private final String name;

    public JedisCondition(String name) {
      this.name = name;
    }

    @Override
    public boolean test(Level level, LoggingContext context) {
      final Condition scriptCondition = cache.get(name);
      if (scriptCondition != null) {
        return scriptCondition.test(level, context);
      }
      return Condition.operational().test(level, context);
    }
  }
}
```

## Docker and Redis instance

You can create a Redis instance using the provided script, which uses Docker under the hood:

```bash
$ ./redis-instance
```

Create an entry in Redis with the key `com.example.Main` and the value of the [info.tf script](info.tf).  I like using [Another Redis Desktop Manager](https://github.com/qishibo/AnotherRedisDesktopManager/releases) for this.

Once you set the script and wait a second, then the script will take priority over the `operational` condition.
