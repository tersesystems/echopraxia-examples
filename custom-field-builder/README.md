# Custom Field Builder

This project demonstrates the use of custom field builders and loggers.  You can easily set up a logger that can render complex domain objects and have them rendered in structured logging:

```java

public class Main {

  // Use a logger with a custom field builder that can log a Person object.
  private static final PersonLogger logger = PersonLoggerFactory.getLogger();

  public static void main(String[] args) throws InterruptedException {
    Person abe = new Person("Abe", 1, "yodelling");
    abe.setFather(new Person("Bert", 35, "keyboards"));
    abe.setMother(new Person("Candace", 30, "iceskating"));

    Condition ageCondition =
        (level, context) ->
            context
                .findNumber("$.person.mother.age")
                .filter(age -> age.intValue() > 30)
                .isPresent();

    logger.info(
        ageCondition,
        "Prints if person's mother age is more than 30",
        fb -> fb.only(fb.person("person", abe)));

    Condition interestsCondition =
        (level, context) -> {
          // the root object is "$." and doesn't have an interests property, so null
          // [null, [yodelling], [keyboards], [iceskating]]
          // Note we get the entire array back here for every match
          List<List<String>> list = context.findList("$..interests");
          return list.stream().anyMatch(i -> i != null && i.get(0).equals("iceskating"));
        };

    logger.info(
        interestsCondition,
        "Prints if someone likes iceskating",
        fb -> fb.only(fb.person("person", abe)));

    logger.info("Custom logging message!", abe);
  }
}
```

Defined using a `PersonLogger`:

```java
public class PersonLogger extends AbstractLoggerSupport<PersonLogger, PersonFieldBuilder>
    implements DefaultLoggerMethods<PersonFieldBuilder> {
  private static final String FQCN = PersonLogger.class.getName();

  protected PersonLogger(
      @NotNull CoreLogger core, @NotNull PersonFieldBuilder fieldBuilder, Class<?> selfType) {
    super(core, fieldBuilder, selfType);
  }

  public void info(@Nullable String message, Person person) {
    // when using custom methods, you must specify the caller as the class it's defined in.
    this.core()
        .withFQCN(FQCN)
        .log(
            Level.INFO,
            message,
            fb -> {
              return fb.only(fb.person("person", person));
            },
            fieldBuilder);
  }

  @Override
  protected @NotNull PersonLogger newLogger(CoreLogger core) {
    return new PersonLogger(core, fieldBuilder(), PersonLogger.class);
  }

  @Override
  protected @NotNull PersonLogger neverLogger() {
    return new PersonLogger(
        core.withCondition(Condition.never()), fieldBuilder(), PersonLogger.class);
  }
}
```

This is fixed to a `PersonFieldBuilder`:

```java
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
```

and the logger factory is likewise simple to put together:

```java
public class PersonLoggerFactory {

  private static final PersonFieldBuilder myFieldBuilder = new PersonFieldBuilder();

  // the class containing the error/warn/info/debug/trace methods
  private static final String FQCN = DefaultLoggerMethods.class.getName();

  public static PersonLogger getLogger(Class<?> clazz) {
    return getLogger(CoreLoggerFactory.getLogger(FQCN, clazz.getName()));
  }

  public static PersonLogger getLogger(String name) {
    return getLogger(CoreLoggerFactory.getLogger(FQCN, name));
  }

  public static PersonLogger getLogger() {
    return getLogger(CoreLoggerFactory.getLogger(FQCN, Caller.resolveClassName()));
  }

  public static PersonLogger getLogger(@NotNull CoreLogger core) {
    return new PersonLogger(core, myFieldBuilder, PersonLogger.class);
  }
}
```