package com.example;

import com.tersesystems.echopraxia.DefaultLoggerMethods;
import com.tersesystems.echopraxia.api.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// Leverages custom field builder
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
            message, fb -> fb.person("person", person),
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
