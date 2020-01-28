scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
  "dev.zio"                      %% "zio"                           % "1.0.0-RC17",
  "com.softwaremill.sttp.client" %% "async-http-client-backend-zio" % "2.0.0-RC6",
  "org.slf4j"                    % "slf4j-nop"                      % "1.7.30",
  "com.lihaoyi"                  %% "upickle"                       % "0.9.5"
)
