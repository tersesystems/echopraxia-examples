package com.example.conditions;

import com.codahale.metrics.Histogram;
import java.util.function.Function;

public class HistogramCondition extends MetricCondition<Histogram> {

  public HistogramCondition(String name, Function<Histogram, Boolean> metricFunction) {
    super(name, metricFunction);
  }

  @Override
  protected Histogram resolveMetric() {
    return metrics.getHistograms().get(getName());
  }
}
