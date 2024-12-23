# Timed Diagnostic Alert

This project demonstrates the use of timed dagnostic alerts, using a circuit breaker from [failsafe](https://failsafe.dev).  The `alertCondition` is set up such that a call to `logger.error` records a failure in the circuit breaker, and after enough failures, the circuit "opens" for five seconds, causing `logger.debug` calls to pass the `levelCondition`.  When there is no more remaining duration, the circuit closes again and goes back to the default logging level.

```java
public class Main {

  private static final DiagnosticAlertManager alerts =
      new DiagnosticAlertManager(
          CircuitBreaker.builder().withFailureThreshold(1).withDelay(Duration.ofSeconds(5)).build(),
          Level.INFO,
          Level.TRACE);

  private static final Logger logger =
      LoggerFactory.getLogger().withCondition(alerts.levelCondition().and(alerts.alertCondition()));

  public static void main(String[] args) throws InterruptedException {
    while (true) {
      try {
        logger.info("logging at INFO"); // enabled by the filter's threshold level
        logger.debug("logging at DEBUG"); // enabled when on diagnostic alert
        logger.trace("logging at TRACE"); // can never be enabled, logback.xml takes priority
        LocalTime now = LocalTime.now();
        int second = now.getSecond();
        if (second == 0 || second == 15 || second == 30 || second == 45) {
          throw new IllegalStateException("Oh no");
        }
      } catch (Exception e) {
        logger.error("Exception happened!", e);
      } finally {
        Thread.sleep(1000L);
      }
    }
  }
}
```
