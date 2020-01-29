package githubprs

import zio.{RIO, UIO}

trait GithubToken {
  val githubToken: GithubToken.Service
}

object GithubToken {
  trait Service {
    val token: UIO[Token]
  }

  object factory {
    val token: RIO[GithubToken, Token] = RIO.accessM(_.githubToken.token)
  }
}
