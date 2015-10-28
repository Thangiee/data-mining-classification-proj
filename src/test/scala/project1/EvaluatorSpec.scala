package project1

import org.scalacheck.Gen
import org.scalactic.TolerantNumerics

class EvaluatorSpec extends BaseSpec {

  val genTestResult = for {
    aa <- Gen.listOfN(25, ("a", "a"))
    ab <- Gen.listOfN(5, ("a", "b"))
    ac <- Gen.listOfN(2, ("a", "c"))
    ba <- Gen.listOfN(3, ("b", "a"))
    bb <- Gen.listOfN(32, ("b", "b"))
    bc <- Gen.listOfN(4, ("b", "c"))
    ca <- Gen.listOfN(1, ("c", "a"))
    cb <- Gen.listOfN(0, ("c", "b"))
    cc <- Gen.listOfN(15, ("c", "c"))
  } yield (aa, ab, ac, ba, bb, bc, ca, cb, cc)

  "Evaluator" must "pass these sanity tests" in {

    forAll(genTestResult) { (testRes) =>
      val (aa, ab, ac, ba, bb, bc, ca, cb, cc) = testRes
      val evaluator = Evaluator(aa ++ ab ++ ac ++ ba ++ bb ++ bc ++ ca ++ cb ++ cc)

      evaluator.truePos("a") shouldEqual aa.size
      evaluator.falsePos("a") shouldEqual (ba ++ ca).size
      evaluator.falseNeg("a") shouldEqual (ab ++ ac).size
      evaluator.trueNeg("a") shouldEqual (bb ++ bc ++ cb ++ cc).size
    }
  }

  it must "pass the example here: https://youtu.be/FAr2GmWNbT0?t=706" in {
    val aa = List.fill(25)(("a", "a"))
    val ab = List.fill(5)(("a", "b"))
    val ac = List.fill(2)(("a", "c"))
    val ba = List.fill(3)(("b", "a"))
    val bb = List.fill(32)(("b", "b"))
    val bc = List.fill(4)(("b", "c"))
    val ca = List.fill(1)(("c", "a"))
    val cb = List.fill(0)(("c", "b"))
    val cc = List.fill(15)(("c", "c"))

    val evaluator = Evaluator(aa ++ ab ++ ac ++ ba ++ bb ++ bc ++ ca ++ cb ++ cc)
    evaluator.truePos("a") shouldEqual 25
    evaluator.falsePos("a") shouldEqual 4
    evaluator.precision("a") shouldEqual 25.0 / (25.0 + 3.0 + 1.0)

    evaluator.recall("b") shouldEqual 32.0 / (32.0 + 3.0 + 4.0)

    implicit val doubleEq = TolerantNumerics.tolerantDoubleEquality(.1)
    evaluator.avgAccuracy shouldEqual (25.0 + 51 + 32.0 + 43 + 15.0 + 65) / (3*(25 + 5 + 2 + 3 + 32 + 4 + 1 + 0 + 15))
  }
}
