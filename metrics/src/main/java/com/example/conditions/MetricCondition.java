package com.example.conditions;

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import echopraxia.api.*;
import echopraxia.logging.api.Condition;
import echopraxia.logging.api.Level;
import echopraxia.logging.api.LoggingContext;
import echopraxia.logging.spi.*;
import java.util.function.Function;

public abstract class MetricCondition<E extends Metric> implements Condition {

  protected static final MetricRegistry metrics = SharedMetricRegistries.getDefault();

  private final String name;
  private final Function<E, Boolean> metricFunction;

  public MetricCondition(String name, Function<E, Boolean> metricFunction) {
    this.name = name;
    this.metricFunction = metricFunction;
  }

  @Override
  public boolean test(Level level, LoggingContext loggingContext) {
    return metricFunction.apply(resolveMetric());
  }

  protected abstract E resolveMetric();

  public String getName() {
    return name;
  }
}
