package project1

import libsvm.LibSVM
import net.sf.javaml.tools.data.FileHandler
import better.files._
import spire.implicits._
import spire.math.Real


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


//  val f = "att.csv".toFile
//  val l = "attLabel.csv".toFile
//
//  (f.lines zip l.lines).foreach {
//    case (attrs, label) =>
//      "att-merge.csv".toFile.createIfNotExists() << s"$label,$attrs"
//  }

  val f = "att-merge.csv".toFile

  val data = FileHandler.loadDataset(f.toJava, 0, ",")
  val svm = new LibSVM

  svm.buildClassifier(data)

  var corr, wrong = 0

  (0 until data.size).foreach { i =>
    val inst = data.instance(i)
    val predict = svm.classify(inst)
    val real = inst.classValue()
    if (predict == real) corr += 1 else wrong  += 1
  }

  println(s"correct: $corr")
  println(s"wrong: $wrong")
//    val predict = svm.classify(d)

//  val (testing, trainingSet) =
//    kFoldPartition(
//      data = (f.lines zip l.lines).toSeq.map(d => parseData(d._1, d._2)),
//      k = 5
//    )
//
//  val data = FileHandler.loadDataset(f.toJava, 0, ",")
//  println(data)
//  val exp = trainingSet.head
//  val predictions = exp.map(test => KNN.classify(exp, test, 5))
//
//  val a = (exp.flatMap(_.label) zip predictions.flatMap(_.label)).toList
//
//  val eva = Evaluator(a)
//
//  println(eva.avgAccuracy, eva.avgF1Score)

}
