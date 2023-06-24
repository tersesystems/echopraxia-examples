package com.example;

import com.tersesystems.echopraxia.Logger;
import com.tersesystems.echopraxia.LoggerFactory;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    var logger = LoggerFactory.getLogger(Main.class);

    while (true) {
      logger.error("logging an error");
      logger.warn("logging a warning");

      logger.info("logging info with herp", fb -> fb.string("name", "herp"));
      logger.info("logging info with derp", fb -> fb.string("name", "derp"));

      logger.debug("logging debug");
      logger.trace("logging trace");

      Thread.sleep(1000L);
    }
  }
}
