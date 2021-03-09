package githubprs.github

import githubprs.githubtoken.GithubToken
import githubprs.{Pull, Repo}
import io.circe
import io.circe.Decoder
import io.circe.generic.auto._
import sttp.client._
import sttp.client.asynchttpclient.zio.AsyncHttpClientZioBackend
import sttp.client.circe._
import zio.{ZIO, ZLayer}

object GithubLive {
  val impl: ZLayer[GithubToken, Throwable, Github] = {
    ZLayer.fromService(githubTokenService =>
      new Github.Service {

        private def get[A: Decoder](path: List[String]) =
          for {
            token <- githubTokenService.token
            response <- AsyncHttpClientZioBackend().flatMap { implicit backend =>
              basicRequest
                .header("Authorization", s"token ${token.value}")
                .get(uri"https://api.github.com/$path")
                .response(asJson[A])
                .send()
            }
          } yield response

        private def toResponse[A](response: Response[Either[ResponseError[circe.Error], A]]) =
          response.body match {
            case Left(DeserializationError(original, e)) =>
              ZIO.fail(
                new RuntimeException(s"Exception deserialising Github API response: $original", e)
              )
            case Left(e) =>
              ZIO.fail(
                new RuntimeException(s"Exception fetching from Github API: ${response.toString}", e)
              )
            case Right(r) => ZIO.succeed(r)
          }

        val repos: ZIO[Any, Throwable, List[Repo]] =
          for {
            repos <- get[List[Repo]](List("user", "repos")) flatMap toResponse
          } yield repos

        def pulls(owner: String, repo: String): ZIO[Any, Throwable, List[Pull]] =
          for {
            pulls <- get[List[Pull]](List("repos", owner, repo, "pulls")) flatMap toResponse
          } yield pulls
      }
    )
  }
}
