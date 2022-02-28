package com.example;

import com.tersesystems.echopraxia.Logger;
import com.tersesystems.echopraxia.LoggerFactory;

public class Main {
  private static final Logger<?> logger = LoggerFactory.getLogger();

  public static void main(String[] args) throws InterruptedException {
    while (true) {
      logger.info("logging only if sqlite condition reached");
      Thread.sleep(1000L);
    }
  }
}
