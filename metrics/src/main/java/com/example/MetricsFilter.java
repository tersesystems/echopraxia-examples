package com.example;

import com.example.conditions.MeterCondition;
import com.tersesystems.echopraxia.api.Condition;
import com.tersesystems.echopraxia.api.CoreLogger;
import com.tersesystems.echopraxia.api.CoreLoggerFilter;

public class MetricsFilter implements CoreLoggerFilter {

  @Override
  public CoreLogger apply(CoreLogger coreLogger) {
    String loggerName = coreLogger.getName();

    if (loggerName.startsWith("com.example.Main.Metrics")) {
      Condition condition = new MeterCondition("requests", m -> m.getMeanRate() > 100);
      return coreLogger.withCondition(condition);
    }
    return coreLogger;
  }
}
