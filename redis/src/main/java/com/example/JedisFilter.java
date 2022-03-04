package com.example;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.tersesystems.echopraxia.Condition;
import com.tersesystems.echopraxia.Level;
import com.tersesystems.echopraxia.LoggingContext;
import com.tersesystems.echopraxia.core.CoreLogger;
import com.tersesystems.echopraxia.core.CoreLoggerFilter;
import redis.clients.jedis.*;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.providers.PooledConnectionProvider;
import redis.clients.jedis.resps.ScanResult;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

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
