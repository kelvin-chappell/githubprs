package githubprs.githubtoken

import githubprs.Token
import zio.{ZIO, ZLayer}

object GithubTokenLive {
  def impl(tokenValue: String): ZLayer[Any, Nothing, GithubToken] = ZLayer.succeed(
    new GithubToken.Service {
      val token: ZIO[Any, Nothing, Token] = ZIO.succeed(Token(tokenValue))
    }
  )
}
