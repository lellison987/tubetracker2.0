name := "tubetracker2.0"

version := "0.1"
val circeVersion = "0.10.0"
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
libraryDependencies += "com.twelvemonkeys.servlet" % "servlet" % "3.4.1"
// https://mvnrepository.com/artifact/com.twelvemonkeys.common/common-lang
libraryDependencies += "com.twelvemonkeys.common" % "common-lang" % "3.4.1"
// https://mvnrepository.com/artifact/com.twelvemonkeys.common/common-io
libraryDependencies += "com.twelvemonkeys.common" % "common-io" % "3.4.1"
// https://mvnrepository.com/artifact/com.twelvemonkeys.common/common-image
libraryDependencies += "com.twelvemonkeys.common" % "common-image" % "3.4.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.18",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.18" % Test
)
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

addCompilerPlugin(
  "org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full
)