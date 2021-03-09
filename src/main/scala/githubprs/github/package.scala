package githubprs

import zio.Has

package object github {
  type Github = Has[Github.Service]
}
