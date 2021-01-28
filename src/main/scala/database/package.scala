package aspects

import zio.Has
import zio._

package object database {

  type Database = Has[Database.Service]

  object Database {

    trait Service {
      def transfer(from: Account, to: Account, amount: Int): IO[TransferError, Unit]
    }

    def inMemory(map: Map[Account, Int]): ZLayer[Any, Nothing, Database] =
      ZLayer.fromEffect {
        Ref.make[Map[Account, Int]](map).map { ref =>
          new Service {
            def transfer(from: Account, to: Account, amount: Int): IO[TransferError, Unit] =
              ref.modify { map =>
                map.get(from) match {
                  case Some(senderBalance) if senderBalance >= amount =>
                    map.get(to) match {
                      case Some(receiverBalance) =>
                        val updated =
                          map +
                            (from -> (senderBalance - amount)) +
                            (to   -> (receiverBalance - amount))
                        (Right(()), updated)
                      case None =>
                        (Left(TransferError.InvalidRecipient(to)), map)
                    }
                  case Some(balance) =>
                    (Left(TransferError.InsufficientFunds(from, amount, balance)), map)
                  case None =>
                    (Left(TransferError.InvalidSender(from)), map)
                }
              }.flatMap(ZIO.fromEither(_))
          }
        }
      }
  }
}
