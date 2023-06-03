package com.example;

import com.tersesystems.echopraxia.api.*;
import java.util.List;

public class CustomFieldMain {

  // Use a logger with a custom field builder that can log a Person object.
  private static final PersonLogger logger = PersonLoggerFactory.getLogger();

  public static void main(String[] args) {
    Person abe = new Person("Abe", 1, "yodelling");
    abe.setFather(new Person("Bert", 35, "keyboards"));
    abe.setMother(new Person("Candace", 30, "iceskating"));

    Condition ageCondition =
        (level, context) ->
            context
                .findNumber("$.person.mother.age")
                .filter(age -> age.intValue() > 30)
                .isPresent();

    // this should not print out because Candace is 30
    logger.info(
        ageCondition,
        "Prints if person's mother age is more than 30",
        fb -> fb.person("person", abe));

    // [[yodelling], [keyboards], [iceskating]]
    // Note we get the entire array back here for every match
    Condition interestsCondition =
        (level, context) ->
            context.findList("$..interests").stream()
                .anyMatch(
                    i -> (i instanceof List) ? ((List<?>) i).get(0).equals("iceskating") : false);

    // log with a field builder
    logger.info(
        interestsCondition, "Prints if someone likes iceskating", fb -> fb.person("person", abe));

    // Can log directly with a PersonLogger
    logger.info("Custom logging message with person {}!", abe);
  }
}
