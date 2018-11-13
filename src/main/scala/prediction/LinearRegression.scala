package prediction

import org.apache.commons.math3.stat.regression.SimpleRegression

object LinearRegression {
  val reg = new SimpleRegression()

  def doRegression(data: Vector[(Int, Int)]): (Double, Double, Double) /*slope, intercept, p-value*/ = {
    val fixedData = data.map(p => Array(p._1.toDouble, p._2.toDouble)).toArray
    reg.addData(fixedData)
    val (m, b, p) = (reg.getSlope, reg.getIntercept, reg.getSignificance)
    reg.clear()
    (m, b, p)
  }

  def produceConstrainedLinePoints(data: Vector[(Int, Int)], mb: (Double, Double)): Vector[(Int, Int)] = {
    val (m, b) = mb
    val minX = data.map(_._1).min
    val maxX = data.map(_._1).max
    val minY = data.map(_._2).min
    val maxY = data.map(_._2).max
    val pts = for{x <- minX to maxX /*if x*m + b >= minY && x*m + b <= maxY*/} yield (x, math.round(x*m + b).toInt)
    pts.toVector
  }

  def produceConstrainedLinePoints(data: Vector[(Int, Int)], mbp: (Double, Double, Double)): Vector[(Int, Int)] =
    produceConstrainedLinePoints(data, (mbp._1, mbp._2))
}
