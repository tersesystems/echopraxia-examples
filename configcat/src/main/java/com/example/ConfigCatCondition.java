package com.example;

import com.configcat.ConfigCatClient;
import com.configcat.User;
import com.tersesystems.echopraxia.Condition;
import com.tersesystems.echopraxia.Level;
import com.tersesystems.echopraxia.LoggingContext;
import java.util.function.Function;

public class ConfigCatCondition implements Condition {

  private final ConfigCatClient client;
  private final Level defaultLevel;
  private final Function<LoggingContext, User> userFunction;
  private final String key;

  public ConfigCatCondition(
      ConfigCatClient client,
      String key,
      Level defaultLevel,
      Function<LoggingContext, User> userFunction) {
    this.client = client;
    this.key = key;
    this.defaultLevel = defaultLevel;
    this.userFunction = userFunction;
  }

  @Override
  public boolean test(Level level, LoggingContext context) {
    User user = userFunction.apply(context);
    String threshold = client.getValue(String.class, key, user, defaultLevel.toString());
    return level.isGreaterOrEqual(Level.valueOf(threshold));
  }
}
