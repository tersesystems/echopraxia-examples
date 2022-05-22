# Custom Condition

You want to match against some nested fields, and you want to use the streams API.

```java
public class Main {
  
  public static void main(String[] args) {
    User steve = new User("steve", 16);
    User will = new User("will", 19);

    logger.info("Do not if user is steve", fb -> fb.user("login", steve));
    logger.info("Log if user is will", fb -> fb.user("login", will));
  }
}
```

Usually the easiest way to match on a condition is to use `ctx.findString` with a JSON path.  However, using JSONPath is a bit slower (400 nanoseconds) than using the streams API.

You can leverage predicates to search through the context using Java's stream API like so:

```java
  static Condition loginCondition = Condition.valueMatch("login",  v -> {
    if (v.type() == Value.Type.OBJECT) {
      return fieldMatch((Value.ObjectValue) v, "name", f -> Objects.equals(f.raw(), "will"));
    } else {
      return false;
    }
  });

  static boolean fieldMatch(Value.ObjectValue v, String fieldName, Predicate<Value<?>> predicate) {
    return v.raw().stream().filter(f -> f.name().equals(fieldName))
      .map(Field::value)
      .anyMatch(predicate);
  }
```