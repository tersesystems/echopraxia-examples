package com.example;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import echopraxia.logger.Logger;
import echopraxia.logger.LoggerFactory;

public class Main {

  private static final MetricRegistry metrics = SharedMetricRegistries.setDefault("default");

  private static final Logger<?> metricsLogger =
      LoggerFactory.getLogger("com.example.Main.Metrics");

  public static void main(String[] args) {
    final Meter requests = metrics.meter("requests");
    requests.mark();
    metricsLogger.info("hello world");
  }
}
