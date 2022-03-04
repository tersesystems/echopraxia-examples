# Redis

Scenario: You want to control logging levels in your application, without restarting it.

Solution: Use Redis and set logging levels from there.

## Implementation

The implementation uses a filter, which connects to Redis to query for the level.

```java
public class JedisFilter implements CoreLoggerFilter, AutoCloseable {

  private final UnifiedJedis client;
  private final Level defaultThreshold = Level.INFO;

  public JedisFilter() {

    HostAndPort config = new HostAndPort(Protocol.DEFAULT_HOST, 6379);
    PooledConnectionProvider provider = new PooledConnectionProvider(config);
    client = new UnifiedJedis(provider);
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
      final String levelString = client.get(name);
      if (levelString != null) {
        final Level threshold = Level.valueOf(levelString);
        return level.isGreaterOrEqual(threshold);
      } else {
        return level.isGreaterOrEqual(defaultThreshold);
      }
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
