package project1

import better.files._
import spire.math.Real
import spire.implicits._

object Main extends App {
  type TestingData = Seq[DataPoint]
  type TrainingDataSet = Seq[Seq[DataPoint]]

  def parseData(attrsLine: String, label: String): DataPoint = {
    DataPoint(Some(label), attrsLine.split(",").map(Real(_)))
  }

  def kFoldPartition(data: Seq[DataPoint], k: Int): (TestingData, TrainingDataSet) = {
    val kFold = data.qshuffled.grouped(data.size/k).toSeq
    (kFold.head, kFold.tail)
  }


  val f = "att.csv".toFile
  val l = "attLabel.csv".toFile

  val (testing, trainingSet) =
    kFoldPartition(
      data = (f.lines zip l.lines).toSeq.map(d => parseData(d._1, d._2)),
      k = 5
    )

  val exp = trainingSet.head
  val predictions = exp.map(test => KNN.classify(exp, test, 5))

  val a = (exp.flatMap(_.label) zip predictions.flatMap(_.label)).toList

  val eva = Evaluator(a)

  println(eva.avgAccuracy, eva.avgF1Score)

}
