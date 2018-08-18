play-silhouette-basic-authentication
====================================

This project guides you through using basic auth in your play project.
Silhouette is a sophisticated framework that can handle multiple types of
authentication but sometimes you just need the simplest one, that is basic auth,
like good old apache, or even with a password stored in your play config.


1. Add dependencies and resolvers in build.sbt
```scala
libraryDependencies ++= Seq(
  "com.mohiva" %% "play-silhouette" % "5.0.5",
  "com.mohiva" %% "play-silhouette-password-bcrypt" % "5.0.5",
  "com.mohiva" %% "play-silhouette-crypto-jca" % "5.0.5",
  "com.mohiva" %% "play-silhouette-persistence" % "5.0.5",
  "com.mohiva" %% "play-silhouette-testkit" % "5.0.5" % "test"
)

resolvers ++= Seq (
          Resolver.jcenterRepo,
         "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
)

```


See full explanation here: http://www.congiu.com/basic-authorization-and-htaccess-style-authentication-on-the-play-framework-an-silhouete/
