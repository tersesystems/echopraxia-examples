package com.customcondition;

import com.tersesystems.echopraxia.Logger;
import com.tersesystems.echopraxia.LoggerFactory;
import com.tersesystems.echopraxia.api.*;

import java.util.Objects;
import java.util.function.Predicate;

public class Main {

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

  static UserFieldBuilder userBuilder = new UserFieldBuilder();

  static Logger<UserFieldBuilder> logger = LoggerFactory.getLogger().withFieldBuilder(userBuilder).withCondition(loginCondition);

  public static void main(String[] args) {
    User steve = new User("steve", 16);
    User will = new User("will", 19);

    logger.info("Do not if user is steve", fb -> fb.user("login", steve));
    logger.info("Log if user is will", fb -> fb.user("login", will));
  }

  static class User {

    private final String name;
    private final Integer age;

    User(String name, Integer age) {
      this.name = name;
      this.age = age;
    }

    public String getName() {
      return name;
    }

    public Integer getAge() {
      return age;
    }
  }

  static class UserFieldBuilder implements FieldBuilder {

    public Value.ObjectValue userValue(User user) {
      Field name = keyValue("name", Value.string(user.getName()));
      Field age = keyValue("age", Value.number(user.getAge()));
      return Value.object(name, age);
    }

    public Field user(String name, User user) {
      return keyValue(name, userValue(user));
    }
  }
}
