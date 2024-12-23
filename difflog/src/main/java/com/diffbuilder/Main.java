package com.diffbuilder;

public class Main {

  public static void main(String[] args) {
    var logger = echopraxia.simple.LoggerFactory.getLogger();
    var fb = PersonFieldBuilder.instance;

    var before = new Person("Eloise", 1);
    var after = before.withName("Will");

    logger.info("Name changed: {}", fb.diff("personDiff", before, after));
  }
}
