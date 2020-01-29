package githubprs

import io.circe.Decoder
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto._

case class Repo(fullName: String, language: Option[String])

object Repo {
  implicit val config: Configuration  = Configuration.default.withSnakeCaseMemberNames.withDefaults
  implicit val decoder: Decoder[Repo] = deriveConfiguredDecoder
}
