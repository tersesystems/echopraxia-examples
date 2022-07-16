package com.diffbuilder;

class Person {
  private final String name;
  private final int age;

  public Person(String name, int age) {
    this.name = name;
    this.age = age;
  }

  public Person withName(String name) {
    return new Person(name, age);
  }

  public Person withAge(int age) {
    return new Person(name, age);
  }

  public Integer getAge() {
    return age;
  }

  public String getName() {
    return name;
  }
}


