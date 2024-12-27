# Echopraxia Examples

This project shows things you can do with [Echopraxia](https://github.com/tersesystems/echopraxia), an application logging framework with structured logging, conditions, context, and filters.  Echopraxia has Log4J2 and Logback implementations and is easy to extend with your own domain loggers and structure.

## Diffing Objects

Scenario: you have a large object with lots of state, and you want to be able to "diff" the object so you can see only what changed in it.

Solution: Turn the object into a value using a field builder, and then diff the `before` and `after` against each other.

Demonstrates custom deserialization for fields and values using Jackson and [zjsonpatch](https://github.com/flipkart-incubator/zjsonpatch/), a JSON patch library. See [README](difflog/README.md).

## Redis Condition Store

Scenario: you want to change logging dynamically, without restarting the application.

Solution: Use conditions and connect to a key/value backend like Redis to process scripts.

Demonstrates dynamic control of logging through Redis and [Caffeine](https://github.com/ben-manes/caffeine). See [README](redis/README.md).

## JMX

Scenario: You want to turn debug logging on and off in an application for individual loggers running locally, from a GUI console. 

Solution: Use JMX flags on conditions, and use [JDK Mission Control](https://github.com/openjdk/jmc#downloading-builds) to change boolean flags attached to loggers.

Demonstrates dynamic control of logging through JMX. See [README](jmx/README.md).

## Scripting 

Scenario: you want dynamic targeted logging using conditions that test against specific arguments in your logging statements.

Solution: Use [script conditions](https://github.com/tersesystems/echopraxia#dynamic-conditions-with-scripts) on the filesystem, and watch for changes in files.

Demonstrates use of Tweakflow scripts, and the ScriptWatchService. See [README](script/README.md).

## SQLite Condition Store

Scenario: You want scripts, but you're running in a production environment where centralized control over logging is important, and using Redis is overkill.

Solution: Keep the scripts in an SQLite database.

Demonstrates dynamic control of scripts kept in an [SQLite database](sqlite.org/) as a backing store.  See [README](conditionstore/README.md).

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

Solution: Use custom field builders and loggers.

Demonstrates use of a custom field builder and fancy loggers.  See [README](custom-field-builder/README.md).

## Custom Conditions

Scenario: You want to add a condition that doesn't depend on JSON Path.

Solution: Use the streaming API.

Demonstrates use of the streaming API to search through the context tree.  See [README](custom-condition/README.md).