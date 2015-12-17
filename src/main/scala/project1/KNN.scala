package project1

import spire.implicits._

object KNN {

  def classify(records: Seq[DataPoint], unknown: DataPoint, k: Int): DataPoint = {
    // sort the list of points by the euclidean distance to the
    // unknown point to obtain the nearest neighbors
    val nn = (records zip records.par.map(p => utils.Distance.euclidean(p.coords, unknown.coords)))
      .qsortedBy { case (_, distToUnknown) => distToUnknown } // sort by the distance

    // predict the unknown label doing majority voting on the k nearest neighbors
    unknown.copy(label = Some(nn.take(k).flatMap(_._1.label).groupBy(identity).maxBy(_._2.size)._1)) //todo: better tie breaker?
  }
}
