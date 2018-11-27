name := "tubetracker2.0"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies += "com.sksamuel.scrimage" %% "scrimage-core" % "2.1.8"
libraryDependencies += "com.sksamuel.scrimage" %% "scrimage-filters" % "2.1.8"
libraryDependencies += "org.bytedeco" % "javacv-platform" % "1.4.3"
libraryDependencies += "org.apache.commons" % "commons-math3" % "3.6.1"
libraryDependencies += "net.imagej" % "ij" % "1.52h"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.18",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.18" % Test
)