package prediction

class LinearPredictor(val alpha: Double = 200.0, val beta: Double = 100.0, val pValueLimit: Double = 0.05) {
  def distanceFromLine(p: (Int, Int), m: Double, b: Double): Double = {
    math.abs(b + m*p._1 - p._2) / math.sqrt(1 + math.pow(m, 2))
  }
  def distanceFromPoint(p1: (Int, Int), p2:(Int, Int)): Double = {
    math.sqrt(math.pow(p2._1 - p1._1, 2) + math.pow(p2._2 - p1._2, 2))
  }
  /*
  Returns a p-value from 0 to 1. alpha controls the decay of the distribution as
  the straight-line distance from the endpoints of the line increases, and beta
  controls the decay of the distribution as the perpendicular distance from the
  extension of the line increases
   */
  def getProbabilityMetric(p: (Int, Int), lineP1: (Int, Int), lineP2: (Int, Int)): Double = {
    val m = (lineP2._2 - lineP1._2).toDouble/(lineP2._1 - lineP1._1)
    val b = lineP1._2 - (m * lineP1._1)
    val dLine = distanceFromLine(p, m, b)
    val dPoint = math.min(distanceFromPoint(p, lineP1), distanceFromPoint(p, lineP2))
    val p1 = math.exp(-1*dPoint/alpha)
    val p2 = math.exp(-1*dLine/beta)
    //println(p, dLine, dPoint, p1, p2)
    p1*p2
  }
  //Bool: significant or not
  def predict(p: (Int, Int), lineP1: (Int, Int), lineP2: (Int, Int)): Boolean =
    getProbabilityMetric(p, lineP1, lineP2) >= (1-pValueLimit)
}
