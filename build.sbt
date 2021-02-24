scalaVersion := "2.13.1"

val sttpV = "2.0.0-RC7"

libraryDependencies ++= Seq(
  "dev.zio"                      %% "zio"                           % "1.0.0-RC17",
  "com.softwaremill.sttp.client" %% "async-http-client-backend-zio" % sttpV,
  "com.softwaremill.sttp.client" %% "circe"                         % sttpV,
  "io.circe"                     %% "circe-generic"                 % "0.12.3",
  "io.circe"                     %% "circe-generic-extras"          % "0.13.0",
  "org.slf4j"                    % "slf4j-nop"                      % "1.7.30"
)
