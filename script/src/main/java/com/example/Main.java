package com.example;

import com.tersesystems.echopraxia.Logger;
import com.tersesystems.echopraxia.LoggerFactory;
import com.tersesystems.echopraxia.api.Condition;
import com.tersesystems.echopraxia.api.Field;
import com.tersesystems.echopraxia.api.PresentationFieldBuilder;
import com.tersesystems.echopraxia.scripting.ScriptCondition;
import com.tersesystems.echopraxia.scripting.ScriptHandle;
import com.tersesystems.echopraxia.scripting.ScriptWatchService;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
  private static final Logger<PresentationFieldBuilder> logger = LoggerFactory.getLogger();
  private final ScriptWatchService sws;
  private final Condition errorCondition;
  private final Condition warnCondition;
  private final Condition infoCondition;

  public static void main(String[] args) throws InterruptedException {
    Path dir = Paths.get("scripting").toAbsolutePath();
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
    logger.warn(
        warnCondition,
        "warning {}",
        fb -> {
          Field name = fb.string("name", "Will");
          Field age = fb.number("age", 23);
          Field interests = fb.array("interests", "reading", "writing", "rithmatic");
          return fb.object("person", name, age, interests);
        });
  }

  public void info() {
    logger.info(
        infoCondition,
        "logging {} {}",
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
