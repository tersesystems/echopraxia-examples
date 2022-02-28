package com.example;

import java.util.Optional;

// Example class with several fields on it.
public class Person {

  private final String name;
  private final int age;
  private final String[] interests;

  private Person father;
  private Person mother;

  Person(String name, int age, String... interests) {
    this.name = name;
    this.age = age;
    this.interests = interests;
  }

  public String name() {
    return name;
  }

  public int age() {
    return age;
  }

  public String[] interests() {
    return interests;
  }

  public void setFather(Person father) {
    this.father = father;
  }

  public Optional<Person> getFather() {
    return Optional.ofNullable(father);
  }

  public void setMother(Person mother) {
    this.mother = mother;
  }

  public Optional<Person> getMother() {
    return Optional.ofNullable(mother);
  }
}
