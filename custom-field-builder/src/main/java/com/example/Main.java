package com.example;

import com.tersesystems.echopraxia.Condition;
import java.util.List;

public class Main {

  // Use a logger with a custom field builder that can log a Person object.
  private static final PersonLogger logger = PersonLoggerFactory.getLogger();

  public static void main(String[] args) throws InterruptedException {
    Person abe = new Person("Abe", 1, "yodelling");
    abe.setFather(new Person("Bert", 35, "keyboards"));
    abe.setMother(new Person("Candace", 30, "iceskating"));

    Condition ageCondition =
        (level, context) ->
            context
                .findNumber("$.person.mother.age")
                .filter(age -> age.intValue() > 30)
                .isPresent();

    logger.info(
        ageCondition,
        "Prints if person's mother age is more than 30",
        fb -> fb.only(fb.person("person", abe)));

    Condition interestsCondition =
        (level, context) -> {
          // the root object is "$." and doesn't have an interests property, so null
          // [null, [yodelling], [keyboards], [iceskating]]
          // Note we get the entire array back here for every match
          List<List<String>> list = context.findList("$..interests");
          return list.stream().anyMatch(i -> i != null && i.get(0).equals("iceskating"));
        };

    logger.info(
        interestsCondition,
        "Prints if someone likes iceskating",
        fb -> fb.only(fb.person("person", abe)));

    logger.info("Custom logging message!", abe);
  }
}
