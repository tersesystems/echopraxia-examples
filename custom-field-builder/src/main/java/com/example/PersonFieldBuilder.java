package com.example;

import com.tersesystems.echopraxia.Field;

import static com.tersesystems.echopraxia.Field.Value;

/**
 * A custom field builder. This is useful for structured logging because typically you want to
 * serialize custom objects to JSON in one place.
 */
public class PersonFieldBuilder implements Field.Builder {
  // Renders a `Person` as an object field.
  public Field person(String fieldName, Person p) {
    return keyValue(fieldName, personValue(p));
  }

  public Value.ObjectValue personValue(Person p) {
    // Note that properties must be broken down to the basic JSON types,
    // i.e. a primitive string/number/boolean/null or object/array.
    Field name = string("name", p.name());
    Field age = number("age", p.age());
    Field father = keyValue("father", Value.optional(p.getFather().map(this::personValue)));
    Field mother = keyValue("mother", Value.optional(p.getMother().map(this::personValue)));
    Field interests = array("interests", p.interests());
    return Value.object(name, age, father, mother, interests);
  }
}
