# Custom Field Builder

This project demonstrates the use of custom field builders.  You can easily set up a logger that can render complex domain objects and have them rendered in structured logging:

```java
public static void main(String[] args) {
    Person abe = new Person("Abe", 1, "yodelling");
    abe.setFather(new Person("Bert", 35, "keyboards"));
    abe.setMother(new Person("Candace", 30, "iceskating"));

    Condition ageCondition =
            pathCondition(
                    (context) ->
                            context
                                    .findNumber("$.person.mother.age")
                                    .filter(age -> age.intValue() > 30)
                                    .isPresent());

    // this should not print out because Candace is 30
    logger
            .withCondition(ageCondition)
            .info("Prints if person's mother age is more than 30", fb.person("person", abe));

    // [[yodelling], [keyboards], [iceskating]]
    // Note we get the entire array back here for every match
    Condition interestsCondition =
            pathCondition(
                    context ->
                            context.findList("$..interests").stream()
                                    .anyMatch(i -> i instanceof List && ((List<?>) i).get(0).equals("iceskating")));

    logger
            .withCondition(interestsCondition)
            .info("Prints if someone likes iceskating", fb.person("person", abe));
}
```

This is fixed to a `PersonFieldBuilder`:

```java
public class PersonFieldBuilder implements Field.Builder {

  static PersonFieldBuilder instance() {
    return new PersonFieldBuilder() {};
  }

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
```
