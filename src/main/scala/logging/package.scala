package aspects

import zio._

package object logging {

  type Logging = Has[Logging.Service]

  object Logging {

    trait Service {
      def logLine(line: String): UIO[Unit]
    }

    val console: ZLayer[Any, Nothing, Logging] =
      ZLayer.succeed {
        new Service {
          def logLine(line: String): UIO[Unit] =
            UIO(println(line))
        }
      }
  }
}
