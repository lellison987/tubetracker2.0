package filters

import com.sksamuel.scrimage.{Image, Pixel}
import objects.ImageTubeList

import scala.annotation.tailrec

object OpenCloseFilter {
  def erode(img: Image, ele: StructuralElement): Image =
    img.toPar.map { case (x, y, v) => if (ele.checkElementIsFullyContained(img, (x, y))) Pixel(255, 255, 255, v.alpha) else Pixel(0, 0, 0, v.alpha) }.toImage

  def dilate(img: Image, ele: StructuralElement): Image =
    img.toPar.map { case (x, y, v) => if (ele.checkElementIsPartiallyContained(img, (x, y))) Pixel(255, 255, 255, v.alpha) else Pixel(0, 0, 0, v.alpha) }.toImage

  @tailrec
  final def nest[A](f: A => A, v: A, n: Int): A = if (n <= 0) f(v) else nest(f, f(v), n - 1)
}
