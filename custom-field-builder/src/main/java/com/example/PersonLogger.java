package com.example;

import com.tersesystems.echopraxia.Condition;
import com.tersesystems.echopraxia.core.CoreLogger;
import com.tersesystems.echopraxia.support.AbstractLoggerSupport;
import com.tersesystems.echopraxia.support.DefaultLoggerMethods;
import org.jetbrains.annotations.NotNull;

// Leverages custom field builder
public class PersonLogger extends AbstractLoggerSupport<PersonLogger, PersonFieldBuilder>
    implements DefaultLoggerMethods<PersonFieldBuilder> {

  protected PersonLogger(
      @NotNull CoreLogger core, @NotNull PersonFieldBuilder fieldBuilder, Class<?> selfType) {
    super(core, fieldBuilder, selfType);
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
