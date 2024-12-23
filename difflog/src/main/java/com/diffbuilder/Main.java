package com.diffbuilder;

import echopraxia.simple.LoggerFactory;

public class Main {

  public static void main(String[] args) {
    var logger = LoggerFactory.getLogger();
    var fb = PersonFieldBuilder.instance;

    var before = new Person("Eloise", 1);
    var after = before.withName("Will");

    logger.info("Name changed: {}", fb.diff("personDiff", before, after));
  }
}
