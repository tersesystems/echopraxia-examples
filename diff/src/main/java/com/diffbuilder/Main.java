package com.diffbuilder;

import com.tersesystems.echopraxia.Logger;
import com.tersesystems.echopraxia.LoggerFactory;

public class Main {

  public static void main(String[] args) {
    Logger<PersonFieldBuilder> logger = LoggerFactory.getLogger().withFieldBuilder(new PersonFieldBuilder());

    Person before = new Person("Eloise", 1);
    Person after = before.withName("Will");

    logger.info("Name changed: {}", fb -> fb.diff("personDiff", before, after));
  }

}
