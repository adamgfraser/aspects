package aspects

import zio._

sealed trait TransferError

object TransferError {

  final case class InvalidSender(account: Account) extends TransferError
  final case class InvalidRecipient(account: Account) extends TransferError
  final case class InsufficientFunds(account: Account, requested: Int, available: Int) extends TransferError
}