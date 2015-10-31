package project1

import java.util.Random

import better.files._
import libsvm.LibSVM
import net.sf.javaml.classification.evaluation.CrossValidation
import net.sf.javaml.tools.data.FileHandler
import project1.utils._
import spire.implicits._

import scala.collection.immutable.IndexedSeq
import scala.io.StdIn._
import scala.util.Try

object Main {
  type TestingData = Seq[DataPoint]
  type TrainingData = Seq[DataPoint]

  def parseData(data: String): DataPoint = {
    val labelAndAttr = data.split(",")
    val (label, attrs) = (labelAndAttr.head, labelAndAttr.tail.map(_.toDouble))
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
    val numOfFolds = Try(readInt()).getOrElse(5)

    print(
      """
        | [1] KNN
        | [2] SVM
        |
        | Choose the classifying algorithm (default=1): """
        .stripMargin)


    if (readLine() == "2") {  // SVM
      import scala.collection.JavaConversions._

      print(
        """
          | [0] linear
          | [1] polynomial
          | [2] radial basis function
          | [3] sigmoid
          |
          | Choose a kernel_type (default=2): """
          .stripMargin)
      val kernel = Try(readInt()).getOrElse(2)

      val defaultGamma = 1.0 / (dataSetFile.lines.next().split(",").length - 1) // 1/num_features
      print(s"\n Choose the gamma value: (default=$defaultGamma): ")
      val gamma = Try(readDouble()).getOrElse(defaultGamma)

      val dataSet = FileHandler.loadDataset(dataSetFile.toJava, 0, ",")

      val svm = new LibSVM
      val params = svm.getParameters
      params.kernel_type = kernel
      params.gamma = gamma
      svm.buildClassifier(dataSet)

      println(svm.getParameters.C, svm.getParameters.gamma)

      val cv = new CrossValidation(svm)
      val results = cv.crossValidation(dataSet, numOfFolds, new Random(System.currentTimeMillis()))

      println(s"\n SVM results (${dataSetFile.name}, k-folds=$numOfFolds, kernel type=$kernel, gamma=$gamma)")
      println(s" ------------------------------------------------------------------------")
      println(" Accuracy = " + results.values().map(_.getAccuracy).toSeq.mean)
      println(" F-score  = " + results.values().map(_.getFMeasure).toSeq.mean)

    } else {  // KNN
      print("\n Choose the number of nearest neighbors (default=5): ")
      val k = Try(readInt()).getOrElse(5)

      val experiments: Seq[(TestingData, TrainingData)] = kFold(dataSetFile.lines.toVector.map(parseData), numOfFolds)

      val results = experiments.zipWithIndex.par.map { case ((testing, training), i) =>
        println(s" > running knn experiment ${i+1}")
        val predictions = testing.map(unknown => KNN.classify(training, unknown, k))
        testing.flatMap(_.label) zip predictions.flatMap(_.label)
      }

      val evaluators = results.map(res => Evaluator(res))
      println(s"\n KNN results (${dataSetFile.name}, k-folds=$numOfFolds, neighbors=$k)")
      println(s" ---------------------------------------------------------")
      println(s"\nAccuracy = ${evaluators.map(_.avgAccuracy).toVector.mean}")
      println(s"F-score  = ${evaluators.map(_.avgF1Score).toVector.mean}")
    }

  }

}
