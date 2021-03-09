package githubprs

import zio.Has

package object githubtoken {
  type GithubToken = Has[GithubToken.Service]
}
