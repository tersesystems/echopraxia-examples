package com.example.conditions;

import com.codahale.metrics.Meter;
import java.util.function.Function;

public class MeterCondition extends MetricCondition<Meter> {

  public MeterCondition(String name, Function<Meter, Boolean> metricFunction) {
    super(name, metricFunction);
  }

  @Override
  protected Meter resolveMetric() {
    return metrics.getMeters().get(getName());
  }
}
