package com.example.conditions;

import com.codahale.metrics.Timer;
import java.util.function.Function;

public class TimerCondition extends MetricCondition<Timer> {

  public TimerCondition(String name, Function<Timer, Boolean> metricFunction) {
    super(name, metricFunction);
  }

  @Override
  protected Timer resolveMetric() {
    return metrics.getTimers().get(getName());
  }
}
