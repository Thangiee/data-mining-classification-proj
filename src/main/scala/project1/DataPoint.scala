package project1

import spire.math.Real

case class DataPoint(label: Option[String] = None, coords: Seq[Real])

object DataPoint {
  def apply(label: String, coords: Real*): DataPoint = DataPoint(Some(label), coords)

}
