package com.diffbuilder;

import echopraxia.logger.Logger;
import echopraxia.logger.LoggerFactory;

public class Main {

  public static void main(String[] args) {
    Logger<PersonFieldBuilder> logger =
        LoggerFactory.getLogger().withFieldBuilder(PersonFieldBuilder.instance);

    Person before = new Person("Eloise", 1);
    Person after = before.withName("Will");

    logger.info("Name changed: {}", fb -> fb.diff("personDiff", before, after));
  }
}
