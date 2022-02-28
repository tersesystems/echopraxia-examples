package com.example;

public class Main {

  // Use a logger with a custom field builder that can log a Person object.
  private static final PersonLogger logger = PersonLoggerFactory.getLogger();

  public static void main(String[] args) throws InterruptedException {
    Person abe = new Person("Abe", 1, "yodelling");
    abe.setFather(new Person("Bert", 35, "keyboards"));
    abe.setMother(new Person("Candace", 30, "iceskating"));

    logger.info("Hi there {}", fb -> fb.only(fb.person("person", abe)));
  }

}
