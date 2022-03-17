package com.example;

import com.tersesystems.echopraxia.Condition;
import com.tersesystems.echopraxia.Level;
import com.tersesystems.echopraxia.core.CoreLogger;
import com.tersesystems.echopraxia.support.AbstractLoggerSupport;
import com.tersesystems.echopraxia.support.DefaultLoggerMethods;
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
