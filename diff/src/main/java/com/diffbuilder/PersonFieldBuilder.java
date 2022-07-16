package com.diffbuilder;

import com.tersesystems.echopraxia.api.*;

public class PersonFieldBuilder implements FieldBuilder, DiffFieldBuilder {

  // Renders a `Person` as an object field.
  public Field person(String fieldName, Person p) {
    return keyValue(fieldName, personValue(p));
  }

  public FieldBuilderResult diffPerson(String name, Person before, Person after) {
    return diff(name, personValue(before), personValue(after));
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
