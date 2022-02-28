# Metrics

This project demonstrates using [dropwizard metrics](https://metrics.dropwizard.io/4.2.0/) in conditions, so that additional logging happens when metrics meet a certain threshold.

First, we'll set up a normal application, with some debugging.

```java
public class Main {

  private static final MetricRegistry metrics = SharedMetricRegistries.setDefault("default");

  private static final Logger<?> metricsLogger = LoggerFactory.getLogger("com.example.Main.Metrics");

  public static void main(String[] args) {
    final Meter requests = metrics.meter("requests");
    requests.mark();
    metricsLogger.info("hello world");
  }
}
```

The metrics filter is applied automatically to the given logger:

```java
public class MetricsFilter implements CoreLoggerFilter {

  @Override
  public CoreLogger apply(CoreLogger coreLogger) {
    String loggerName = coreLogger.getName();

    if (loggerName.equals("com.example.Main.Metrics")) {
      Condition condition = new MeterCondition("requests", m -> m.getMeanRate() > 100);
      return coreLogger.withCondition(condition);
    }
    return coreLogger;
  }
}
```

Here, the condition will look for the `requests` meter, and only return true if the mean rate is over 100.  This means that the metrics logger will only output when there are enough requests, even though the logging level itself is `DEBUG` in the `logback.xml` file.


