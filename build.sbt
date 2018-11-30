name := "tubetracker2.0"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies += "com.sksamuel.scrimage" %% "scrimage-core" % "2.1.8"
libraryDependencies += "com.sksamuel.scrimage" %% "scrimage-filters" % "2.1.8"
libraryDependencies += "org.bytedeco" % "javacv-platform" % "1.4.3"
libraryDependencies += "org.apache.commons" % "commons-math3" % "3.6.1"
// https://mvnrepository.com/artifact/com.twelvemonkeys.imageio/imageio-core
libraryDependencies += "com.twelvemonkeys.imageio" % "imageio-core" % "3.4.1"
// https://mvnrepository.com/artifact/com.twelvemonkeys.imageio/imageio-jpeg
libraryDependencies += "com.twelvemonkeys.imageio" % "imageio-jpeg" % "3.4.1"
// https://mvnrepository.com/artifact/com.twelvemonkeys.imageio/imageio-tiff
libraryDependencies += "com.twelvemonkeys.imageio" % "imageio-tiff" % "3.4.1"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.18",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.18" % Test
)
