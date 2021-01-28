package aspects

import zio._
import zio.stm._

final case class Account(identifier: Long)