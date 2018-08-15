name := "play_silhouette_basic_auth"

version := "1.0"

lazy val `play_silhouette_basic_auth` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

scalaVersion := "2.12.2"

libraryDependencies ++= Seq(
  jdbc, ehcache, ws, specs2 % Test, guice,
  // Silhouette dependencies
  "com.mohiva" %% "play-silhouette" % "5.0.5",
  "com.mohiva" %% "play-silhouette-password-bcrypt" % "5.0.5",
  "com.mohiva" %% "play-silhouette-crypto-jca" % "5.0.5",
  "com.mohiva" %% "play-silhouette-persistence" % "5.0.5",
  "com.mohiva" %% "play-silhouette-testkit" % "5.0.5" % "test",
  "org.scalatest" % "scalatest_2.12" % "3.0.5" % "test",

  // use scala syntax on guice vs java
  "net.codingwell" %% "scala-guice" % "4.2.1"
)

resolvers ++= Seq(
  Resolver.jcenterRepo,
  "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
)



unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")

      