ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.12"

lazy val root = (project in file("."))
  .settings(
    name := "scala-example",
    resolvers += Resolver.mavenLocal,
    libraryDependencies += "org.scala-lang.modules" %% "scala-java8-compat" % "1.0.2",
    libraryDependencies += "com.tersesystems.echopraxia" %% "scala-sourcecode" % "1.5.0-SNAPSHOT",
    libraryDependencies += "com.tersesystems.echopraxia" % "logstash" % "1.5.0-SNAPSHOT",
    libraryDependencies += "com.tersesystems.blacklite" % "blacklite-logback" % "1.1.0",
    libraryDependencies += "com.tersesystems.logback" % "logback-uniqueid-appender" % "1.0.2",
  )
