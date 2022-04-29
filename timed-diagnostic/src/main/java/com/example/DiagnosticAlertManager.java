package com.example;

import com.tersesystems.echopraxia.api.*;
import dev.failsafe.CircuitBreaker;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * This class uses a circuit breaker to elevate logging to a diagnostic level for a set period of
 * time following an error.
 */
public class DiagnosticAlertManager {

  private final CircuitBreaker<?> breaker;
  private final Level alertLevel;
  private final Level defaultLevel;

  public DiagnosticAlertManager(CircuitBreaker<?> breaker, Level defaultLevel, Level alertLevel) {
    if (defaultLevel.isLessOrEqual(alertLevel)) {
      String msg =
          "Default level " + defaultLevel + " must be higher than alert level " + alertLevel;
      throw new IllegalArgumentException(msg);
    }

    this.breaker = breaker;
    this.alertLevel = alertLevel;
    this.defaultLevel = defaultLevel;
  }

  public DiagnosticAlertManager(CircuitBreaker<?> breaker) {
    this(breaker, Level.INFO, Level.TRACE);
  }

  public Condition levelCondition() {
    return new LevelCondition();
  }

  public Condition alertCondition() {
    return new AlertCondition();
  }

  final class LevelCondition implements Condition {
    @Override
    public boolean test(Level level, LoggingContext context) {
      // close the breaker after the delay has expired, since
      // we're not recording success or result on the breaker.
      if (breaker.getRemainingDelay() == Duration.ZERO) {
        breaker.close();
      }
      Level threshold = breaker.isClosed() ? defaultLevel : alertLevel;
      return level.isGreaterOrEqual(threshold);
    }
  }

  /**
   * The alert condition records a failure in the circuit breaker internally if isFailure is true.
   */
  final class AlertCondition implements Condition {
    @Override
    public boolean test(Level level, LoggingContext context) {
      if (isFailure(level, context)) {
        Optional<Throwable> optField = findException(context);
        if (optField.isPresent()) {
          Throwable ex = optField.get();
          breaker.recordException(ex);
        } else {
          breaker.recordFailure();
        }
      }
      return true;
    }

    private boolean isFailure(Level level, LoggingContext context) {
      return level == Level.ERROR;
    }

    @NotNull
    private Optional<Throwable> findException(LoggingContext context) {
      List<Field> fields = context.getFields();
      for (Field f : fields) {
        if (f.value().type() == Value.Type.EXCEPTION) {
          return Optional.of((Throwable) f.value().raw());
        }
      }
      return Optional.empty();
    }
  }
}
