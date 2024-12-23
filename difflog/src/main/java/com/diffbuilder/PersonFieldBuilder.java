package com.diffbuilder;

import echopraxia.api.*;

public class PersonFieldBuilder implements DiffFieldBuilder {

  private PersonFieldBuilder() {}

  public static final PersonFieldBuilder instance = new PersonFieldBuilder();

  public Field diff(String name, Person before, Person after) {
    return diff(name, personValue(before), personValue(after), DefaultField.class);
  }

  public Value<?> personValue(Person p) {
    if (p == null) {
      return Value.nullValue();
    }
    Field name = string("name", p.getName());
    Field age = number("age", p.getAge());
    return Value.object(name, age);
  }
}
