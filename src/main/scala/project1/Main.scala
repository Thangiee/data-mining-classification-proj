package project1

import spire.syntax.literals._
import better.files._
import spire.math.Real
import spire.implicits._

object Main extends App {

  def parseData(attrsLine: String, label: String): DataPoint = {
    DataPoint(Some(label), attrsLine.split(",").map(Real(_)))
  }

  val f = "iris.csv".toFile
  val l = "irisLabel.csv".toFile


}
