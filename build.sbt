lazy val akkaVersion = "2.5.13"
lazy val scalaJHttpVersion = "2.4.0"
lazy val mysqlVersion = "8.0.11"
lazy val scalikeJdbcVersion = "3.2.3"
lazy val tfBindingVersion = "2.1.18"
lazy val logbackVersion = "1.2.3"
lazy val scalaLoggingVersion = "3.9.0"
lazy val googleApiClientVersion = "1.23.0"
lazy val scalaTestVersion = "3.0.5"

lazy val root = (project in file("."))
  .settings(
    name := "weatherstation",

    scalaVersion := "2.12.6",
    organization := "io.tafli",

    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
      "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
      "org.scalikejdbc" %% "scalikejdbc" % scalikeJdbcVersion,
      "org.scalikejdbc" %% "scalikejdbc-config" % scalikeJdbcVersion,
      "org.scalaj" %% "scalaj-http" % scalaJHttpVersion,
      "mysql" % "mysql-connector-java" % mysqlVersion,
      "com.tinkerforge" % "tinkerforge" % tfBindingVersion,
      "ch.qos.logback" % "logback-classic" % logbackVersion,
      "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion,
      "com.google.api-client" % "google-api-client" % googleApiClientVersion,
      "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
    )
  )
