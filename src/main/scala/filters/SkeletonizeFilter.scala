package filters

import com.sksamuel.scrimage.{Image, Pixel}

import scala.annotation.tailrec

class SkeletonizeFilter(threshold: Int = 127) extends Filter {
  // https://ieeexplore.ieee.org/document/7233099/?part=1
  // I can't define my own filters, scrimage? Seriously?
  def apply(image: Image): Image = skeletonize(image)

  private val pxOrder = Vector((0, 0), (0, 1), (1, 1), (1, 0), (1, -1), (0, -1), (-1, -1), (-1, 0), (-1, 1))

  private implicit def bool2int(b: Boolean): Int = if (b) 1 else 0

  private def B(neighborhoodVals: Vector[Int]): Int = neighborhoodVals.tail.count(_ >= threshold) //since the original value is at index zero, the tail comprises the neighborhood of (x, y)

  private def C(neighborhoodVals: Vector[Int]): Int = {
    val b1: Int = neighborhoodVals(1) < threshold && (neighborhoodVals(2) >= threshold || neighborhoodVals(3) >= threshold)
    val b2: Int = neighborhoodVals(3) < threshold && (neighborhoodVals(4) >= threshold || neighborhoodVals(5) >= threshold)
    val b3: Int = neighborhoodVals(5) < threshold && (neighborhoodVals(6) >= threshold || neighborhoodVals(7) >= threshold)
    val b4: Int = neighborhoodVals(7) < threshold && (neighborhoodVals(8) >= threshold || neighborhoodVals(1) >= threshold)
    b1 + b2 + b3 + b4
  }

  private def getNeighborhoodValues(x: Int, y: Int, img: Image): Vector[Int] = {
    val surroundings = pixelsAroundCenter(x, y, (i, j) => (math.min(math.max(i, 0), img.width - 1), math.min(math.max(j, 0), img.height - 1)))
    surroundings.map(px => pxMagnitude(img.pixel(px)))
  }

  private def pixelsAroundCenter(x: Int, y: Int, f: (Int, Int) => (Int, Int) = (i: Int, j: Int) => (i, j)): Vector[(Int, Int)] = {
    for ((i, j) <- pxOrder) yield f(x + i, y + j)
  }

  private def pxMagnitude(px: Pixel): Int = (px.red + px.blue + px.green) / 3

  private def iterate(image: Image, iteration: Boolean = true): Image = image.map { case (x, y, px) =>
      val sur = getNeighborhoodValues(x, y, image)
      val c = C(sur)
      val b = B(sur)
      val newPx = if (
        iteration &&
          (x + y) % 2 == 0 &&
          c == 1 &&
          b >= 2 &&
          b <= 7 &&
          sur(1) * sur(3) * sur(5) == 0 &&
          sur(3) * sur(5) * sur(7) == 0
          ||
          !iteration &&
            (x + y) % 2 == 1 &&
            c == 1 &&
            b >= 2 &&
            b <= 7 &&
            sur(1) * sur(3) * sur(5) == 0 &&
            sur(3) * sur(5) * sur(7) == 0
      ) Pixel(0, 0, 0, 255) else px
      newPx

  }


@tailrec
private def skeletonize(image: Image, iteration: Boolean = true): Image = {
  val newimg = iterate(image, iteration)
  if (newimg.bytes sameElements image.bytes) image
  else skeletonize(newimg, !iteration)
}

}
