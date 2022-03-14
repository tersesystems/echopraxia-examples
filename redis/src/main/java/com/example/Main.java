package com.example;

import com.tersesystems.echopraxia.*;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    Logger<?> logger = LoggerFactory.getLogger(Main.class);

    while (true) {
      logger.error("logging an error");
      logger.warn("logging a warning");
      logger.info("logging info");
      logger.debug("logging debug");
      logger.trace("logging trace");

      Thread.sleep(1000L);
    }
  }
}
