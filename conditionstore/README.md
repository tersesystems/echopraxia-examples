## Condition Store

This project sets up an [SQLite database](sqlite.org/) as a backing store for [script conditions](https://github.com/tersesystems/echopraxia#dynamic-conditions-with-scripts).


What this means in practice is that you can have a logger operate at an INFO level:

```
package com.example;

import echopraxia.simple.Logger;
import echopraxia.simple.LoggerFactory;

public class App {
  private static final Logger logger = LoggerFactory.getLogger();

  public static void main(String[] args) throws InterruptedException {
    while (true) {
      logger.info("logging only if sqlite condition reached");
      Thread.sleep(1000L);
    }
  }

}
```

and attach a condition using a [filter]()

```
public class SqliteConditionFilter implements CoreLoggerFilter {
  private final JdbcConditionStore conditionStore;

  public SqliteConditionFilter() {
    try {
      // https://github.com/xerial/sqlite-jdbc#how-to-specify-database-files
      // Need to specify files as sqlite / postgresql etc
      final ResourceBundle bundle = ResourceBundle.getBundle("echopraxia.sqlite");
      final String jdbcUrl = "jdbc:sqlite:conditions.db";

      conditionStore =
        new JdbcConditionStore(
          true,
          jdbcUrl,
          bundle,
          (name, e) -> {
            e.printStackTrace();
          });

      conditionStore.runDDL(bundle);
    } catch (SQLException e) {
      throw new ScriptException(e);
    }
  }

  @Override
  public CoreLogger apply(CoreLogger coreLogger) {
    final Condition condition = conditionStore.create(coreLogger.getName());
    return coreLogger.withCondition(condition);
  }
}
```

Then if you go into the `conditions.db` database and change the `script` field from the default, it will be picked up and run by the logger automatically.

Because sqlite can be [replicated across multiple instances](https://github.com/rqlite/rqlite), you can manage complex logging conditions for an entire service at once, without redeploying or bouncing your application.
