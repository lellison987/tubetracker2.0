package filters

import com.sksamuel.scrimage.{Image, Pixel}

case class Kernel(kernel: Array[Array[Double]], edgeBehavior: Int = Kernel.EXTRAPOLATE_EDGES) {
  require(kernel.length % 2 == 1 && kernel.head.length % 2 == 1, "filter.Kernel must have odd dimension values")
  def apply(x: Int, y: Int, img: Image): Pixel = {
    edgeBehavior match {
      case Kernel.EXTRAPOLATE_EDGES =>
        val curPx = img.pixel(x, y)
        val surroundings = pixelsAroundCenter(x, y, (i, j) => (math.min(math.max(i, 0), img.width - 1), math.min(math.max(j, 0), img.height - 1)))
        computeNewPixel(surroundings, curPx, img)

      case Kernel.IGNORE_EDGES =>
        val curPx = img.pixel(x, y)
        val surroundings = pixelsAroundCenter(x, y)
        if(surroundings.exists{case (i, j) => i < 0 || i >= img.width || j < 0 || j >= img.height}) Pixel(0,0,0,curPx.alpha)
        else computeNewPixel(surroundings, curPx, img)

      case _ => throw new IllegalArgumentException("Operation not supported!")
    }
  }

  private def pixelsAroundCenter(x: Int, y: Int, f: (Int, Int) => (Int, Int) = (i: Int, j: Int) => (i, j)): Seq[(Int, Int)] = {
    val dX = kernel.length / 2
    val dY = kernel.head.length / 2
    for{
      j <- y - dY to y + dY
      i <- x - dX to x + dX
    } yield f(i,j)
  }

  private def computeNewPixel(surroundings: Seq[(Int, Int)], curPx: Pixel, img: Image): Pixel = {
    val surroundingsWithScale = surroundings.zip(kernel.flatten.toIterable)
    val newPx = surroundingsWithScale.foldLeft((0, 0, 0)){
      case (accum: (Int, Int, Int), (px: (Int, Int), scale: Double)) =>
        (
          accum._1 + (img.pixel(px).red * scale).toInt,
          accum._2 + (img.pixel(px).green * scale).toInt,
          accum._3 + (img.pixel(px).blue * scale).toInt
        )
    }
    //println(s"(${curPx.red},${curPx.green},${curPx.blue}) -> (${boundPx(newPx._1)},${boundPx(newPx._2)},${boundPx(newPx._3)})")

    Pixel(
      boundPx(newPx._1),
      boundPx(newPx._2),
      boundPx(newPx._3),
      curPx.alpha
    )
  }

  private def boundPx(v: Int): Int = math.min(math.max(v, 0), 0xFF)
}

object Kernel {
  val IGNORE_EDGES = 0
  val EXTRAPOLATE_EDGES = 1

  implicit class ImageKernelOps(val img: Image) {
    def applyKernel(kern: Kernel): Image = img.map((x, y, px) => kern(x, y, img))
  }
}