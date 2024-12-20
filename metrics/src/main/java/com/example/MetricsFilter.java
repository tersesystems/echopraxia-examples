package com.example;

import com.example.conditions.MeterCondition;
import echopraxia.api.*;
import echopraxia.logging.api.Condition;
import echopraxia.logging.spi.CoreLogger;
import echopraxia.logging.spi.CoreLoggerFilter;

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
