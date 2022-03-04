package com.example;

import com.tersesystems.echopraxia.Condition;
import com.tersesystems.echopraxia.Level;
import com.tersesystems.echopraxia.LoggingContext;
import com.tersesystems.echopraxia.core.CoreLogger;
import com.tersesystems.echopraxia.core.CoreLoggerFilter;
import redis.clients.jedis.*;
import redis.clients.jedis.providers.PooledConnectionProvider;

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
