package com.example;

import com.tersesystems.echopraxia.Logger;
import com.tersesystems.echopraxia.LoggerFactory;

public class Main {

  private static final Logger<?> logger = LoggerFactory.getLogger();

  public static void main(String[] args) {
    Logger<?> idLogger = logger.withFields(fb -> fb.onlyString("configcat_email", "will@tersesystems.com"));
    idLogger.info("I believe that logging is a right");

  }
}
