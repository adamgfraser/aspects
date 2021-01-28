package aspects

import zio._

import aspects.database._
import aspects.logging._

trait Aspect[-R, +E] {
  def apply[R1 <: R, E1 >: E, A](zio: ZIO[R1, E1, A]): ZIO[R1, E1, A]
}

object Aspect {

  val logging: Aspect[Database with Logging, Nothing] =
    new Aspect[Database with Logging, Nothing] {
      def apply[R <: Database with Logging, E, A](zio: ZIO[R, E, A]): ZIO[R, E, A] =
        ZIO.environment[Logging].flatMap { logging =>
          zio.updateService[Database.Service] { database =>
            new Database.Service {
              def transfer(from: Account, to: Account, amount: Int): IO[TransferError, Unit] =
                database
                  .transfer(from, to, amount)
                  .tap(_ => logging.get.logLine(s"transferred $amount from $from to $to"))
            }
          }
        }
    }
}
