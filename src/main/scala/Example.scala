package aspects

import zio._
import zio.random.Random

import aspects.database._
import aspects.logging._

object Example extends App {
  import aspects.Aspect._

  val alice = new Account(0)
  val bob = new Account(1)

  val database = Database.inMemory(Map(alice -> 100, bob -> 100))

  def transfer(from: Account, to: Account, amount: Int): ZIO[Database, TransferError, Unit] =
    ZIO.accessM[Database](_.get.transfer(from, to, amount))

  def run(args: List[String]): URIO[ZEnv, ExitCode] =
    effect.provideLayer(database ++ Logging.console).exitCode

  val effect =
    transfer(alice, bob, 100) @@ logging
}