package project1

import java.util.Random

import libsvm.LibSVM
import net.sf.javaml.core.Dataset
import net.sf.javaml.tools.data.FileHandler
import better.files._
import spire.implicits._
import spire.math.Real

import scala.collection.immutable.IndexedSeq
import scala.io.StdIn._
import scala.reflect.ClassTag
import scala.util.Try

object Main {
  type TestingData = Seq[DataPoint]
  type TrainingData = Seq[DataPoint]

  def parseData(data: String): DataPoint = {
    val labelAndAttr = data.split(",")
    val (label, attrs) = (labelAndAttr.head, labelAndAttr.tail.map(Real(_)))
    DataPoint(Some(label), attrs)
  }

  def kFold(data: Seq[DataPoint], k: Int): Seq[(TestingData, TrainingData)] = {
    val folds: IndexedSeq[Seq[DataPoint]] = data.qshuffled.grouped(data.size / k).toIndexedSeq

    (0 until k).map { i =>
      (folds(i), folds.flatten.diff(folds(i)))
    }
  }

  def main(args: Array[String]) {
    print(
      """
        | [1] iris
        | [2] iris normalize
        | [3] att
        | [4] att normalize
        | [5] banknote authentication
        | [6] banknote authentication normalize
        |
        | Choose a data set (default=1): """
        .stripMargin)

    val dataSetFile = readLine() match {
      case "1" => "iris-with-label.csv".toFile
      case "2" => "iris-normalize-with-label.csv".toFile
      case "3" => "att-with-label.csv".toFile
      case "4" => "att-normalize-with-label.csv".toFile
      case "5" => "data_banknote_authentication.csv".toFile
      case "6" => "data_banknote_authentication_normalize.csv".toFile
      case _   => "iris-with-label.csv".toFile
    }

    print("\n Choose the number of k-folds (default=5): ")
    val k = Try(readInt()).getOrElse(5)

    print(
      """
        | [1] KNN
        | [2] SVM
        |
        | Choose the classifying algorithm (default=1): """
        .stripMargin)


    if (readLine() == "2") {
      val experiments = FileHandler.loadDataset(dataSetFile.toJava, 0, ",").folds(k, new Random(System.currentTimeMillis()))

      import scala.collection.JavaConversions._
      println(experiments.head.diff(experiments.tail.head).size)
//      val svm = new LibSVM

//      svm.buildClassifier(a.head._1.asInstanceOf[Dataset])

    } else {

      val experiments: Seq[(TestingData, TrainingData)] = kFold(dataSetFile.lines.toVector.map(parseData), k)
      val results = experiments.zipWithIndex.map { case ((testing, training), i) =>
        println(s" > running experiment $i")
        val predictions = testing.map(unknown => KNN.classify(training, unknown, 5))
        testing.flatMap(_.label) zip predictions.flatMap(_.label)
      }

      results.foreach(res => println(Evaluator(res).avgAccuracy))
    }

  }



  //  val f = "iris-with-label.csv".toFile
  //
  //  val data = FileHandler.loadDataset(f.toJava, 0, ",")
  //  val svm = new LibSVM
  //
  //  svm.buildClassifier(data)
  //
  //  var corr, wrong = 0
  //
  //  (0 until data.size).foreach { i =>
  //    val inst = data.instance(i)
  //    val predict = svm.classify(inst)
  //    val real = inst.classValue()
  //    if (predict == real) corr += 1 else wrong  += 1
  //  }
  //
  //  println(s"correct: $corr")
  //  println(s"wrong: $wrong")


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
