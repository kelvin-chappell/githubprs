scalaVersion := "2.13.10"

val sttpV = "2.3.0"

libraryDependencies ++= Seq(
  "dev.zio"                      %% "zio"                           % "1.0.18",
  "com.softwaremill.sttp.client" %% "async-http-client-backend-zio" % sttpV,
  "com.softwaremill.sttp.client" %% "circe"                         % sttpV,
  "io.circe"                     %% "circe-generic"                 % "0.13.0",
  "io.circe"                     %% "circe-generic-extras"          % "0.13.0",
  "org.slf4j"                     % "slf4j-nop"                     % "2.0.6"
)
