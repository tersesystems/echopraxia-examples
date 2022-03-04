# Redis

Scenario: You want to control logging levels in your application, without restarting it.

Solution: Use Redis and set logging levels from there.

## Implementation

The implementation uses a filter, which connects to Redis to query for the level and caches the result using [Caffeine](https://github.com/ben-manes/caffeine).

```java
public class JedisFilter implements CoreLoggerFilter, AutoCloseable {

  private final JedisPooled client;
  private final Level defaultThreshold = Level.INFO;
  private final LoadingCache<String, Level> cache;

  public JedisFilter() {
    HostAndPort config = new HostAndPort(Protocol.DEFAULT_HOST, 6379);
    PooledConnectionProvider provider = new PooledConnectionProvider(config);
    client = new JedisPooled(provider);

    // Set up cache and refresh
    cache = Caffeine.newBuilder()
      .maximumSize(10_000)
      .refreshAfterWrite(Duration.ofSeconds(10)) // async call to redis if > 10 seconds old
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

  private Level queryRedis(String key) {
    String result = client.get(key);
    return result == null ? defaultThreshold : Level.valueOf(result);
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
      final Level threshold = cache.get(name);
      return level.isGreaterOrEqual(threshold);
    }
  }
}
```

## Docker and Redis instance

You can create a Redis instance using the provided script, which uses Docker under the hood:

```bash
$ ./redis-instance
```

Create an entry setting the `com.example.Main` logger to `DEBUG`.

```bash
$ ./redis-cli 
# redis-cli
127.0.0.1:6379> ping
PONG
127.0.0.1:6379> set com.example.Main DEBUG
OK
127.0.0.1:6379> get com.example.Main
"DEBUG"
127.0.0.1:6379> exit
```

You can delete entries in Redis:

```bash
127.0.0.1:6379> del com.example.Main
(integer) 1
```
