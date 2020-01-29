package githubprs

import zio.RIO

trait Github {
  val github: Github.Service
}

object Github {
  trait Service {
    val repos: RIO[GithubToken, List[Repo]]
    def pulls(owner: String, repo: String): RIO[GithubToken, List[Pull]]
  }

  object factory {
    val repos: RIO[GithubToken with Github, List[Repo]] = RIO.accessM(_.github.repos)
    def pulls(owner: String, repo: String): RIO[GithubToken with Github, List[Pull]] =
      RIO.accessM(_.github.pulls(owner, repo))
  }
}
