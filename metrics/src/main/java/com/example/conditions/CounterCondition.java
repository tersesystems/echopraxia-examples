package com.example.conditions;

import com.codahale.metrics.Counter;
import java.util.function.Function;

public class CounterCondition extends MetricCondition<Counter> {

  public CounterCondition(String name, Function<Counter, Boolean> metricFunction) {
    super(name, metricFunction);
  }

  @Override
  protected Counter resolveMetric() {
    return metrics.getCounters().get(getName());
  }
}
