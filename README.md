# Echopraxia Examples

This project shows things you can do with [Echopraxia](https://github.com/tersesystems/echopraxia).

## Async Logging

Demonstrates [AsyncLogger](https://github.com/tersesystems/echopraxia#asynchronous-logging) with correct caller info.  See [README](async/README.md).

## Redis

Demonstrates dynamic control of logging through Redis.  See [README](redis/README.md).

## Condition Store

Demonstrates dynamic control of logging through [script conditions](https://github.com/tersesystems/echopraxia#dynamic-conditions-with-scripts) kept in an [SQLite database](sqlite.org/) as a backing store.  See [README](conditionstore/README.md).

## Timed Diagnostic

Demonstrates changing logging to diagnostic level (DEBUG/TRACE) for a set duration in response to error messages.  See [README](timed-diagnostic/README.md).

## Metrics

Demonstrates logging in response to unusual [dropwizard metrics](https://metrics.dropwizard.io/4.2.0/) conditions.  See [README](metrics/README.md).

## System Info

Demonstrates a [filter](https://github.com/tersesystems/echopraxia#filters) to add CPU and memory system information fields to the logger context on every logger call, using [OSHI](https://github.com/oshi/oshi).  See [README](system-info/README.md).

## Custom Field Builder and Logger

Demonstrates use of a custom field builder and fancy loggers.  See [README](custom-field-builder/README.md).

