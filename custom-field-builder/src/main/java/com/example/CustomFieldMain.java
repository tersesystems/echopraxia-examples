package com.example;

import static echopraxia.logging.api.JsonPathCondition.pathCondition;

import echopraxia.logging.api.Condition;
import echopraxia.simple.Logger;
import echopraxia.simple.LoggerFactory;
import java.util.List;

public class CustomFieldMain {

  private static final Logger logger = LoggerFactory.getLogger();
  private static final PersonFieldBuilder fb = PersonFieldBuilder.instance();

  public static void main(String[] args) {
    Person abe = new Person("Abe", 1, "yodelling");
    abe.setFather(new Person("Bert", 35, "keyboards"));
    abe.setMother(new Person("Candace", 30, "iceskating"));

    Condition ageCondition =
        pathCondition(
            (context) ->
                context
                    .findNumber("$.person.mother.age")
                    .filter(age -> age.intValue() > 30)
                    .isPresent());

    // this should not print out because Candace is 30
    logger
        .withCondition(ageCondition)
        .info("Prints if person's mother age is more than 30", fb.person("person", abe));

    // [[yodelling], [keyboards], [iceskating]]
    // Note we get the entire array back here for every match
    Condition interestsCondition =
        pathCondition(
            context ->
                context.findList("$..interests").stream()
                    .anyMatch(i -> i instanceof List && ((List<?>) i).get(0).equals("iceskating")));

    // log with a field builder
    logger
        .withCondition(interestsCondition)
        .info("Prints if someone likes iceskating", fb.person("person", abe));
  }
}
