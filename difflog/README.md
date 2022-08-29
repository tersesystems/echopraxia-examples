# Diff Field Builder

You can take on object, change its state and "diff" it against the previous state.  This can be very useful when you have a large object and want to isolate only the changes over time.

This functionality is possible because `Field` and `Value` have custom serializers built into Jackson.  JSON-Patch libraries like [zjsonpatch](https://github.com/flipkart-incubator/zjsonpatch/) is free.

```java
public interface DiffFieldBuilder {

  default FieldBuilderResult diff(String fieldName, Value<?> before, Value<?> after) {
    JsonNode beforeNode = mapper.valueToTree(before);
    JsonNode afterNode = mapper.valueToTree(after);

    JsonNode patch = JsonDiff.asJson(beforeNode, afterNode);
    // convert the patch json node back to fields and values :-)
    final Value<?> value = mapper.convertValue(patch, Value.class);
    return Field.keyValue(fieldName, value);
  }
}

class DiffFieldBuilderConstants {
  static ObjectMapper mapper = new ObjectMapper();
  static {
    // should register EchopraxiaModule here because of jackson dependency
    mapper.findAndRegisterModules();
  }
}
```

and then plug it into your own field builder:

```java
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
```

and you get

```scala
18:01:49.353 [INFO ] com.diffbuilder.Main -  Name changed: personDiff=[{op=replace, path=/name, value=Will}]
```