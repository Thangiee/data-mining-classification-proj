package project1.preprocessing

import better.files._
import project1.utils._

object Preprocessor {

  def normalize(data: Seq[Double]): Seq[Double] = {
    val mean = data.mean
    val sd   = data.stddev
    data.map(x => (x - mean) / sd)
  }

  def main(args: Array[String]) {
    val in = "data_banknote_authentication.csv".toFile
    val out = "data_banknote_authentication_normalize.csv".toFile.createIfNotExists()

    val data = in.lines.toVector.map(_.split(",").map(_.toDouble))

    data.transpose    // group by attributes
      .map(normalize) // normalize the attrs
      .transpose      // restore to the original grouping by instances
      .foreach(inst => out << s"${inst.map(_.roundTo(5)).mkString(",")}")
  }
}
