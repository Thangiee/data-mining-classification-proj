package project1

import java.text.{DecimalFormat, DecimalFormatSymbols}
import java.util.Locale

package object utils {

  implicit class Rounding(number: Double) {
    def roundTo(DecimalPlace: Int): Double = {
      if (number.isNaN) return 0.0
      new DecimalFormat("###." + ("#" * DecimalPlace), new DecimalFormatSymbols(Locale.US)).format(number).toDouble
    }
  }

  implicit class NumberStatsOps[N: Numeric](xs: Seq[N]) {
    def mean = Stats.mean(xs)
    def devs = Stats.devs(xs)
    def stddev = Stats.stddev(xs)
  }
}
