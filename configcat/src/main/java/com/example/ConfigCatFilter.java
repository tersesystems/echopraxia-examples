package com.example;

import com.configcat.ConfigCatClient;
import com.configcat.LogLevel;
import com.configcat.User;
import com.tersesystems.echopraxia.Field;
import com.tersesystems.echopraxia.Field.Value.ValueType;
import com.tersesystems.echopraxia.LoggingContext;
import com.tersesystems.echopraxia.core.CoreLogger;
import com.tersesystems.echopraxia.core.CoreLoggerFilter;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class ConfigCatFilter implements CoreLoggerFilter, AutoCloseable {

  private final ConfigCatClient client;

  public ConfigCatFilter() {
    client = ConfigCatClient.newBuilder()
            .logLevel(LogLevel.INFO)
            .build("PKDVCLf-Hq-h-kCzMp-L7Q/HhOWfwVtZ0mb30i9wi17GQ");

  }

  @Override
  public CoreLogger apply(CoreLogger coreLogger) {
    String key = coreLogger.getName();
    boolean defaultValue = false;
    Function<LoggingContext, User> userFunction = ctx -> {
      String email = email(ctx);
      Map<String, String> customAttributes = attributesFromContext(ctx);
      return User.newBuilder().email(email).custom(customAttributes).build(email);
    };
    ConfigCatCondition condition = new ConfigCatCondition(client, key, defaultValue, userFunction);
    return coreLogger.withCondition(condition);
  }

  private Map<String, String> attributesFromContext(LoggingContext ctx) {
    return Collections.emptyMap();
  }

  private String email(LoggingContext ctx) {
    Optional<? extends Field.Value<?>> opt = ctx.getFields().stream().filter(f -> f.name().equals("configcat_email")).map(Field::value).findFirst();
    if (opt.isPresent()) {
      Field.Value<?> value = opt.get();
      if (value.type() == ValueType.STRING) {
        return (String) value.raw();
      }
    }
    return "user@example.org";
  }

  @Override
  public void close() throws Exception {
    client.close();
  }
}
