package githubprs

import zio._
import zio.console.Console

object Main extends App {

  val owner = "guardian"
  val repo  = "support-service-lambdas"

  def run(args: List[String]): ZIO[ZEnv, Nothing, Int] =
    program
      .provide(new Console.Live with GithubLive with GithubToken {
        val githubToken: GithubToken.Service = new GithubToken.Service {
          val token: UIO[Token] = UIO.succeed(Token(args(0)))
        }
      })
      .tapError(e => console.putStrLn(e.toString))
      .fold(_ => 1, _ => 0)

  val program: RIO[Console with GithubToken with Github, Unit] =
    for {
      repos <- Github.factory.repos
      //pulls <- Github.factory.pulls(owner, repo)
      _ <- console.putStrLn(repos.toString)
    } yield ()
}
