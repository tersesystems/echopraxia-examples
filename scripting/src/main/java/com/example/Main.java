package com.example;

import com.tersesystems.echopraxia.Condition;
import com.tersesystems.echopraxia.Logger;
import com.tersesystems.echopraxia.LoggerFactory;
import com.tersesystems.echopraxia.scripting.ScriptCondition;
import com.tersesystems.echopraxia.scripting.ScriptHandle;
import com.tersesystems.echopraxia.scripting.ScriptWatchService;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
  private static final Logger<?> logger = LoggerFactory.getLogger();
  private final ScriptWatchService sws;
  private final Condition infoCondition;
  private final Condition errorCondition;

  public static void main(String[] args) throws InterruptedException {
    Path dir = Paths.get("scripting").toAbsolutePath();
    Main main = new Main(dir);
    main.doStuff();
  }

  public Main(Path dir) {
    sws = new ScriptWatchService(dir);
    infoCondition = createCondition(dir.resolve("info.tf"));
    errorCondition = createCondition(dir.resolve("error.tf"));
  }

  public void doStuff() throws InterruptedException {
    while (true) {
      try {
        info();
        error();
      } finally {
        Thread.sleep(1000L);
      }
    }
  }

  public void info() {
    logger.info(
      infoCondition,
      "logging at INFO",
      fb -> fb.list(fb.string("name", "Eloise"), fb.number("age", 1)));
  }

  public void error() {
    logger.error(errorCondition, "Some error", new RuntimeException("Some Message"));
  }

  private Condition createCondition(Path file) {
    ScriptHandle scriptHandle = sws.watchScript(file, Throwable::printStackTrace);
    return ScriptCondition.create(scriptHandle);
  }
}
