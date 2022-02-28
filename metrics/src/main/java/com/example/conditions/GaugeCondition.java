package com.example.conditions;

import com.codahale.metrics.Gauge;
import java.util.function.Function;

public class GaugeCondition<T> extends MetricCondition<Gauge<T>> {

  private final Class<Gauge<T>> gaugeClass;

  public GaugeCondition(
      String name, Class<Gauge<T>> gaugeClass, Function<Gauge<T>, Boolean> metricFunction) {
    super(name, metricFunction);
    this.gaugeClass = gaugeClass;
  }

  @Override
  protected Gauge<T> resolveMetric() {
    return gaugeClass.cast(metrics.getGauges().get(getName()));
  }
}
