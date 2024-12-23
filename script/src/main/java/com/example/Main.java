package com.example;

import echopraxia.api.FieldBuilderResult;
import echopraxia.logging.api.Condition;
import echopraxia.scripting.ScriptCondition;
import echopraxia.scripting.ScriptHandle;
import echopraxia.scripting.ScriptWatchService;
import echopraxia.simple.Logger;
import echopraxia.simple.LoggerFactory;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
  private static final Logger logger = LoggerFactory.getLogger();
  private final ScriptWatchService sws;
  private final Condition errorCondition;
  private final Condition warnCondition;
  private final Condition infoCondition;

  public static void main(String[] args) throws InterruptedException {
    Path dir = Paths.get("script").toAbsolutePath();
    Main main = new Main(dir);
    main.doStuff();
  }

  public Main(Path dir) {
    sws = new ScriptWatchService(dir);
    errorCondition = createCondition(dir.resolve("error.tf"));
    warnCondition = createCondition(dir.resolve("warn.tf"));
    infoCondition = createCondition(dir.resolve("info.tf"));
  }

  public void doStuff() throws InterruptedException {
    while (true) {
      try {
        error();
        warn();
        info();
      } finally {
        Thread.sleep(2000L);
      }
    }
  }

  public void warn() {
    logger
        .withCondition(warnCondition)
        .warn(
            "warning {}",
            FieldBuilderResult.apply(
                () -> {
                  var fb = logger.fieldBuilder();
                  var name = fb.string("name", "Will");
                  var age = fb.number("age", 23);
                  var interests = fb.array("interests", "reading", "writing", "rithmatic");
                  return fb.object("person", name, age, interests);
                }));
  }

  public void info() {
    var fb = logger.fieldBuilder();
    logger
        .withCondition(infoCondition)
        .info("logging {} {}", fb.list(fb.string("name", "Eloise"), fb.number("age", 1)));
  }

  public void error() {
    logger.withCondition(errorCondition).error("Some error", new RuntimeException("Some Message"));
  }

  private Condition createCondition(Path file) {
    ScriptHandle scriptHandle = sws.watchScript(file, Throwable::printStackTrace);
    return ScriptCondition.create(scriptHandle);
  }
}
