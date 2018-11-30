package objects

case class Tubule(points: Vector[(Int, Int)], p1: (Int, Int), p2: (Int, Int)) {
  def length: Double = math.sqrt(math.pow(p2._1 - p1._1, 2) + math.pow(p2._2 - p1._2, 2))
}
