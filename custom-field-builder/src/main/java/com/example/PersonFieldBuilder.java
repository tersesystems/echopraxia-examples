package com.example;

import com.tersesystems.echopraxia.api.*;

import java.util.Optional;

/**
 * A custom field builder. This is useful for structured logging because typically you want to
 * serialize custom objects to JSON in one place.
 */
public interface PersonFieldBuilder extends FieldBuilder {

  static PersonFieldBuilder instance() {
    return new PersonFieldBuilder() {};
  }

  // Renders a `Person` as an object field.
  default Field person(String fieldName, Person p) {
    return keyValue(fieldName, personValue(p));
  }

  default Value<?> personValue(Person p) {
    if (p == null) return Value.nullValue();
    // Note that properties must be broken down to the basic JSON types,
    // i.e. a primitive string/number/boolean/null or object/array.
    Field name = string("name", p.name());
    Field age = number("age", p.age());
    Field father = keyValue("father", personValue(p.getFather()));
    Field mother = keyValue("mother", personValue(p.getMother()));
    Field interests = array("interests", p.interests());
    return Value.object(name, age, father, mother, interests);
  }

  default Value<?> personValue(Optional<Person> p) {
    return Value.optional(p.map(this::personValue));
  }
}
