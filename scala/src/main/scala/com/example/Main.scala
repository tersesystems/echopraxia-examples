package com.example


import java.time.Instant

object Main {

  def main(args: Array[String]): Unit = {
    val thing = new MyThing()
    thing.doStuff()
  }
}

object MyThing {
  private val logger = CustomLoggerFactory.getLogger
}

class MyThing {

  import MyThing._

  logger.info("this is a test")
  logger.info("this is a test with a function {}", fb =>
    fb.list(fb.array("name", Seq("herp", "derp")))
  )

  // XXX should be able to auto convert java conditions to scala condition
  logger.info("{} {} {} {}", fb => {
    import fb._
    list(
      obj("person" -> Seq(
        value("numberName" -> 1),
        value("bool" -> true),
        keyValue("instant" -> Instant.now()),
        array("ints" -> Seq(1, 2, 3)),
        keyValue("strName" -> "bar")
      )),
      value("foo", Instant.now()),
      instant("timestamp" -> Instant.now()),
      obj("person", Map(
        "herp" -> "derp",
        "foo" -> "bar"
      ))
    )
  })

  def doStuff(): Unit = {
    logger.info("what about source code info? {}", fb => fb.list {
      fb.instant("foo" -> Instant.now())
    })
  }
}
