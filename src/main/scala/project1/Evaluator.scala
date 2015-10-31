package project1

import project1.Evaluator._
import cats.std.all._
import cats.syntax.eq._

import scala.collection._

case class Evaluator(testResult: Seq[(RealLabel, ClassifyLabel)]) {

  private val labels = testResult.map(_._1).distinct
  private val confusionMatrix = new mutable.HashMap[(String, String), Int] // todo: could probability be made immutable

  // build the confusionMatrix
  for (i <- labels; j <- labels) { confusionMatrix.put((i, j), testResult.count(_ ==(i, j))) }

  def truePos(label: String): Int = confusionMatrix.get(label, label).getOrElse(0)

  def falsePos(label: String): Int = labels.flatMap(row => confusionMatrix.get(row, label)).sum - truePos(label)

  def trueNeg(label: String): Int =
    (for {
      row    <- labels
      column <- labels
    } yield if (!(row == label || column == label)) confusionMatrix.get(row, column) else None).flatten.sum

  def falseNeg(label: String): Int = labels.flatMap(column => confusionMatrix.get(label, column)).sum - truePos(label)

  def precision(label: String): Double = truePos(label).toDouble / (truePos(label) + falsePos(label))

  def recall(label: String): Double = truePos(label).toDouble / (truePos(label) + falseNeg(label))

  def f1Score(label: String): Double = {
    2 * (precision(label) * recall(label) / (precision(label) + recall(label)))
  }

  def avgF1Score: Double = {
    val f1Scores = labels.map(f1Score).filterNot(_ === Double.NaN)
    f1Scores.sum / f1Scores.size
  }

  def accuracy(label: String): Double =
    (truePos(label) + trueNeg(label)).toDouble / (truePos(label) + trueNeg(label) + falsePos(label) + falseNeg(label))

  def avgAccuracy: Double = labels.map(accuracy).sum / labels.size
}

object Evaluator {
  type RealLabel = String
  type ClassifyLabel = String
}
