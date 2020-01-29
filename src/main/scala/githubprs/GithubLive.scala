package githubprs

import io.circe
import io.circe.Decoder
import io.circe.generic.auto._
import sttp.client._
import sttp.client.asynchttpclient.zio.AsyncHttpClientZioBackend
import sttp.client.circe._
import zio.RIO

trait GithubLive extends Github {
  val github: Github.Service = new Github.Service {

    private def get[A: Decoder](path: List[String]) =
      for {
        token <- GithubToken.factory.token
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
          RIO.fail(
            new RuntimeException(s"Exception deserialising Github API response: $original", e)
          )
        case Left(e) =>
          RIO.fail(
            new RuntimeException(s"Exception fetching from Github API: ${response.toString}", e)
          )
        case Right(r) => RIO.succeed(r)
      }

    val repos: RIO[GithubToken, List[Repo]] =
      for {
        repos <- get[List[Repo]](List("user", "repos")) flatMap toResponse
      } yield repos

    def pulls(owner: String, repo: String): RIO[GithubToken, List[Pull]] =
      for {
        pulls <- get[List[Pull]](List("repos", owner, repo, "pulls")) flatMap toResponse
      } yield pulls
  }
}

object GithubLive extends GithubLive
