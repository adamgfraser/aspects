package aspects

import zio.stream._

trait StreamAspect[-R, +E] {
  def apply[R1 <: R, E1 >: E, O](stream: ZStream[R1, E1, O]): ZStream[R1, E1, O]
}

object StreamAspect {

  def chunk(n: Int): StreamAspectPoly =
    new StreamAspectPoly {
      def apply[R, E, O](stream: ZStream[R, E, O]): ZStream[R, E, O] =
        stream.chunkN(n)
    }
}
