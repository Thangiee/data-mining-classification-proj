package project1.utils

import spire.implicits._
import spire.math._

object Distance {
  type Coordinates = Seq[Double]

  def manhattan(coords1: Coordinates, coords2: Coordinates): Real = minkowski(coords1, coords2, 1)

  def euclidean(coords1: Coordinates, coords2: Coordinates): Real = minkowski(coords1, coords2, 2)

  private def minkowski(coords1: Coordinates, coords2: Coordinates, h: Int): Real =
    (coords1 zip coords2).map(dim => abs(dim._1 - dim._2).pow(h)).qsum.nroot(h)
}
