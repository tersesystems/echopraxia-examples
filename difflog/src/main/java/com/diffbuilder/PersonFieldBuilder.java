package com.diffbuilder;

import com.tersesystems.echopraxia.api.*;
import com.tersesystems.echopraxia.diff.DiffFieldBuilder;

public class PersonFieldBuilder implements FieldBuilder, DiffFieldBuilder {

  private PersonFieldBuilder() {}

  public static final PersonFieldBuilder instance = new PersonFieldBuilder();

  public PresentationField diff(String name, Person before, Person after) {
    return diff(name, personValue(before), personValue(after), PresentationField.class);
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
