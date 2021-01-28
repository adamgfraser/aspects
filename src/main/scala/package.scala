import zio._
import zio.stream._

package object aspects {

  type AspectPoly = Aspect[Any, Nothing]
  type StreamAspectPoly = StreamAspect[Any, Nothing]

  implicit final class AspectSyntax[-R, +E, +A](private val zio: ZIO[R, E, A]) extends AnyVal {
    def @@[R1 <: R, E1 >: E](aspect: Aspect[R1, E1]): ZIO[R1, E1, A] =
      aspect(zio)
  }

  implicit final class StreamAspectSyntax[-R, +E, +O](private val stream: ZStream[R, E, O]) extends AnyVal {
    def @@[R1 <: R, E1 >: E](aspect: StreamAspect[R1, E1]): ZStream[R1, E1, O] =
      aspect(stream)
  }
}