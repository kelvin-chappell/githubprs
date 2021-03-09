package githubprs.github

import githubprs.{Pull, Repo}
import zio.ZIO

object Github {
  trait Service {
    val repos: ZIO[Any, Throwable, List[Repo]]
    def pulls(owner: String, repo: String): ZIO[Any, Throwable, List[Pull]]
  }

  val repos: ZIO[Github, Throwable, List[Repo]] = ZIO.accessM(_.get.repos)
  def pulls(owner: String, repo: String): ZIO[Github, Throwable, List[Pull]] =
    ZIO.accessM(_.get.pulls(owner, repo))
}
