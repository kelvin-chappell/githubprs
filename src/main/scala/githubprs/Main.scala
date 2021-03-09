package githubprs

import githubprs.github.{Github, GithubLive}
import githubprs.githubtoken.GithubTokenLive
import zio._
import zio.console.Console

object Main extends zio.App {

  val owner = "guardian"
  val repo  = "support-service-lambdas"

  def run(args: List[String]): URIO[ZEnv, ExitCode] =
    program
      .provideCustomLayer(GithubTokenLive.impl(args.head) >>> GithubLive.impl)
      .tapError(e => console.putStrLn(e.toString))
      .fold(_ => ExitCode.failure, _ => ExitCode.success)

  val program: ZIO[Console with Github, Throwable, Unit] =
    for {
      repos <- Github.repos
      //pulls <- Github.factory.pulls(owner, repo)
      _ <- console.putStrLn(repos.toString)
    } yield ()
}
