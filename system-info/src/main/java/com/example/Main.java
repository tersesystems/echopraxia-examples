package com.example;

import echopraxia.simple.Logger;
import echopraxia.simple.LoggerFactory;

public class Main {

  private static final Logger logger = LoggerFactory.getLogger();

  public static void main(String[] args) throws InterruptedException {
    while (true) {
      try {
        logger.info("logging at INFO");
      } finally {
        Thread.sleep(1000L);
      }
    }
  }
}
