package com.example;

import static com.jayway.jsonpath.Criteria.where;
import static com.jayway.jsonpath.Filter.filter;

import com.jayway.jsonpath.Filter;
import com.tersesystems.echopraxia.Condition;

import java.util.List;
import java.util.Map;

public class Main {

  // Use a logger with a custom field builder that can log a Person object.
  private static final PersonLogger logger = PersonLoggerFactory.getLogger();

  public static void main(String[] args) throws InterruptedException {
    Person abe = new Person("Abe", 1, "yodelling");
    abe.setFather(new Person("Bert", 35, "keyboards"));
    abe.setMother(new Person("Candace", 30, "iceskating"));

    Condition abeCondition =
        (level, context) -> {
          // Language Injections in IntelliJ IDEA and add LoggingContext
          Number age = context.find(Number.class, "$.person.mother.age");
          return age.equals(30);
        };
    logger.info(abeCondition, "Hi there {}", fb -> fb.only(fb.person("person", abe)));
  }
}
