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

    logger.info("Hi there {}", fb -> fb.only(fb.person("person", abe)));
  }

}
```

Defined using a `PersonLogger`:

```java
public class PersonLogger extends AbstractLoggerSupport<PersonLogger, PersonFieldBuilder>
  implements DefaultLoggerMethods<PersonFieldBuilder> {

  protected PersonLogger(@NotNull CoreLogger core, @NotNull PersonFieldBuilder fieldBuilder, Class<?> selfType) {
    super(core, fieldBuilder, selfType);
  }

  @Override
  protected @NotNull PersonLogger newLogger(CoreLogger core) {
    return new PersonLogger(core, fieldBuilder(), PersonLogger.class);
  }

  @Override
  protected @NotNull PersonLogger neverLogger() {
    return new PersonLogger(core.withCondition(Condition.never()), fieldBuilder(), PersonLogger.class);
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

  public static PersonLogger getLogger() {
    return getLogger(CoreLoggerFactory.getLogger(FQCN, Caller.resolveClassName()));
  }

  public static PersonLogger getLogger(@NotNull CoreLogger core) {
    return new PersonLogger(core, myFieldBuilder, PersonLogger.class);
  }
}
```