# Echopraxia Examples

This project shows things you can do with [Echopraxia](https://github.com/tersesystems/echopraxia), an application logging framework with structured logging, conditions, context, and filters.  Echopraxia has Log4J2 and Logback implementations and is easy to extend with your own domain loggers and structure.

## Async Logging

Scenario: you want to log conditionally, but you don't want to incur the costs of evaluating the condition in your rendering thread.

Solution: Use asynchronous logging, which will do all logging in another thread.

Demonstrates [AsyncLogger](https://github.com/tersesystems/echopraxia#asynchronous-logging) with correct caller info.  See [README](async/README.md).

## Redis

Scenario: you want to change logger levels dynamically, without restarting the application.

Solution: Use conditions and connect to a key/value backend like Redis to evaluate levels. 

Demonstrates dynamic control of logging through Redis and [Caffeine](https://github.com/ben-manes/caffeine). See [README](redis/README.md).

## JMX

Scenario: You want to turn debug logging on and off in an application for individual loggers running locally, from a GUI console. 

Solution: Use JMX flags on conditions, and use [JDK Mission Control](https://github.com/openjdk/jmc#downloading-builds) to change boolean flags attached to loggers.

Demonstrates dynamic control of logging through JMX. See [README](jmx/README.md).

## Condition Store

Scenario: you want dynamic targeted logging using conditions that test against specific arguments in your logging statements.

Solution: Use scripts, and keep the scripts in a central location so they can be easily stored and updated.

Demonstrates dynamic control of logging through [script conditions](https://github.com/tersesystems/echopraxia#dynamic-conditions-with-scripts) kept in an [SQLite database](sqlite.org/) as a backing store.  See [README](conditionstore/README.md).

## Timed Diagnostic

Scenario: You want to automatically turn up logging to DEBUG level for a while, when an error happens, so you can gather more diagnostic information.

Solution: Use a filter and a circuit breaker condition.

Demonstrates changing logging to diagnostic level (DEBUG/TRACE) for a set duration in response to error messages.  See [README](timed-diagnostic/README.md).

## Metrics

Scenario: You want to log some additional statements when a metric is exceeded.

Solution: Use a metric condition.

Demonstrates logging in response to unusual [dropwizard metrics](https://metrics.dropwizard.io/4.2.0/) conditions.  See [README](metrics/README.md).

## System Info

Scenario: You want to add some system information to every statement.

Solution: Use a filter and OSHI to add additional fields to logging context.

Demonstrates a [filter](https://github.com/tersesystems/echopraxia#filters) to add CPU and memory system information fields to the logger context on every logger call, using [OSHI](https://github.com/oshi/oshi).  See [README](system-info/README.md).

## Custom Field Builder and Logger

Scenario: You want to log some complex domain objects as structured fields, along with custom logging statements.

Solution: Use custom field builders.

Demonstrates use of a custom field builder and fancy loggers.  See [README](custom-field-builder/README.md).

