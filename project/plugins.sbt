// The Typesafe repository
resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/maven-releases/"

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.0-M2")
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.2.0-M8")

logLevel := Level.Warn
