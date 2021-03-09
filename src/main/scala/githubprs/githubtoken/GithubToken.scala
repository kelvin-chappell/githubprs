package githubprs.githubtoken

import githubprs.Token
import zio.ZIO

object GithubToken {
  trait Service {
    val token: ZIO[Any, Nothing, Token]
  }

  val token: ZIO[GithubToken, Nothing, Token] = ZIO.accessM(_.get.token)
}
