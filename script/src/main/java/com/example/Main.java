package com.example;

import com.tersesystems.echopraxia.Condition;
import com.tersesystems.echopraxia.Field;
import com.tersesystems.echopraxia.Logger;
import com.tersesystems.echopraxia.LoggerFactory;
import com.tersesystems.echopraxia.scripting.ScriptCondition;
import com.tersesystems.echopraxia.scripting.ScriptHandle;
import com.tersesystems.echopraxia.scripting.ScriptWatchService;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
  private final Logger<?> logger;
  
  private final ScriptWatchService sws;
  private final Condition infoCondition;

  public static void main(String[] args) throws InterruptedException {
    Path dir = Paths.get(".").toAbsolutePath();
    Main main = new Main(dir);
    main.doStuff();
  }

  public Main(Path dir) {
    sws = new ScriptWatchService(dir);
    infoCondition = createCondition(dir.resolve("op.tf"));
    logger = LoggerFactory.getLogger().withCondition(infoCondition);
  }

  public void doStuff() throws InterruptedException {
    while (true) {
      try {
        error();
        warn();
        info();
        debug();
      } finally {
        Thread.sleep(2000L);
      }
    }
  }

  public void debug() {
    logger.debug("some debug statement");
  }

  public void warn() {
    logger.warn(
        "warning {}",
        fb -> {
          Field name = fb.string("name", "Will");
          Field age = fb.number("age", 23);
          Field interests = fb.array("interests", "reading", "writing", "rithmatic");
          return fb.onlyObject("person", name, age, interests);
        });
  }

  public void info() {
    logger.info(
        "logging {} {}",
        fb -> fb.list(fb.string("name", "Eloise"), fb.number("age", 1)));
  }

  public void error() {
    logger.error("Some error", new RuntimeException("Some Message"));
  }

  private Condition createCondition(Path file) {
    ScriptHandle scriptHandle = sws.watchScript(file, Throwable::printStackTrace);
    return ScriptCondition.create(scriptHandle);
  }
}
