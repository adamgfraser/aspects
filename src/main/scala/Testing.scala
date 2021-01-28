import zio._
import zio.test._
import zio.test.Assertion._
import zio.test.TestAspect.nonFlaky

object Testing {

  val scatteredAndTangled =
    testM("foreachPar preserves ordering") {
      val zio = ZIO.foreach(1 to 100) { _ =>
        ZIO.foreachPar(1 to 100)(ZIO.succeed(_)).map(_ == (1 to 100))
      }.map(_.forall(identity))
      assertM(zio)(isTrue)
    }

  val separated =
  testM("foreachPar preserves ordering") {
    assertM(ZIO.foreachPar(1 to 100)(ZIO.succeed(_)))(equalTo(1 to 100))
  } @@ nonFlaky(100)
}