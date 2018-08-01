addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.8")
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.6")


libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.46"
addSbtPlugin("org.scalikejdbc" %% "scalikejdbc-mapper-generator" % "3.3.0")