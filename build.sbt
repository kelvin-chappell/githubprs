scalaVersion := "2.13.10"

val sttpV = "2.2.9"

libraryDependencies ++= Seq(
  "dev.zio"                      %% "zio"                           % "1.0.5",
  "com.softwaremill.sttp.client" %% "async-http-client-backend-zio" % sttpV,
  "com.softwaremill.sttp.client" %% "circe"                         % sttpV,
  "io.circe"                     %% "circe-generic"                 % "0.14.4",
  "io.circe"                     %% "circe-generic-extras"          % "0.13.0",
  "org.slf4j"                     % "slf4j-nop"                     % "1.7.36"
)
