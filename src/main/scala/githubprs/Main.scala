package githubprs

import sttp.client._
import sttp.client.asynchttpclient.zio.AsyncHttpClientZioBackend
import upickle.default.read
import zio.console.{Console, putStrLn}
import zio.{App, RIO, Task, ZEnv, ZIO}

object Main extends App {

  val owner = "guardian"
  val repo  = "support-service-lambdas"

  def run(args: List[String]): ZIO[ZEnv, Nothing, Int] =
    program(args).tapError(e => putStrLn(e.toString)).fold(_ => 1, _ => 0)

  def get(
      maybeToken: Option[String],
      path: List[String]
  ): RIO[Console, Response[Either[String, String]]] =
    AsyncHttpClientZioBackend().flatMap { implicit backend =>
      val unauthed = basicRequest.get(uri"https://api.github.com/$path")
      val request = maybeToken.fold(unauthed) { token =>
        unauthed.header("Authorization", s"token $token")
      }
      putStrLn(unauthed.toCurl).flatMap { _ =>
        request.send()
      }
    }

  def getUnauthorised(path: List[String]): RIO[Console, Response[Either[String, String]]] =
    get(None, path)

  def getAuthorised(
      token: String,
      path: List[String]
  ): RIO[Console, Response[Either[String, String]]] =
    get(Some(token), path)

  val getZen: RIO[Console, Response[Either[String, String]]] = getUnauthorised(List("zen"))

  def getUser(token: String, userName: String): RIO[Console, Response[Either[String, String]]] =
    getAuthorised(token, List("users", userName))

  def getPulls(token: String): RIO[Console, Response[Either[String, String]]] =
    getAuthorised(token, List("repos", owner, repo, "pulls"))

  def stringify(response: Response[Either[String, String]]): Task[String] = {
    import upickle.default._
    val body = response.body.fold(identity, identity)
    for {
      x <- Task.effect(read[List[Map[String,String]]](body))
    } yield s"${response.code.code}: ${response.body.fold(identity, identity)}\n\n\n$x"
  }

  def program(args: List[String]): ZIO[Console, Throwable, Unit] =
    for {
      token <- ZIO.effect(args(0))
      pulls <- getPulls(token)
      s     <- stringify(pulls)
      _     <- putStrLn(s)
    } yield ()
}
