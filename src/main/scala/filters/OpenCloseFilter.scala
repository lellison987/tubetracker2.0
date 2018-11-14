package filters

import com.sksamuel.scrimage.{Image, Pixel}

import scala.annotation.tailrec

object OpenCloseFilter {
  def erode(img: Image, ele: StructuralElement): Image =
    img.map { case (x, y, v) => if (ele.checkElementIsFullyContained(img, (x, y))) Pixel(255, 255, 255, v.alpha) else Pixel(0, 0, 0, v.alpha) }

  def dilate(img: Image, ele: StructuralElement): Image =
    img.map { case (x, y, v) => if (ele.checkElementIsPartiallyContained(img, (x, y))) Pixel(255, 255, 255, v.alpha) else Pixel(0, 0, 0, v.alpha) }

  def erodeInPlace(img: Image, ele: StructuralElement, pt: (Int, Int)): Image = {
    if (ele.checkElementIsFullyContained(img, (pt._1, pt._2)))
      img.setPixel(pt._1, pt._2, Pixel(255, 255, 255, img.pixel(pt).alpha))
    else
      img.setPixel(pt._1, pt._2, Pixel(0, 0, 0, img.pixel(pt).alpha))
    img
  }

  def dilateInPlace(img: Image, ele: StructuralElement, pt: (Int, Int)): Image = {
    if (ele.checkElementIsPartiallyContained(img, (pt._1, pt._2)))
      img.setPixel(pt._1, pt._2, Pixel(255, 255, 255, img.pixel(pt).alpha))
    else
      img.setPixel(pt._1, pt._2, Pixel(0, 0, 0, img.pixel(pt).alpha))
    img
  }


  @tailrec
  final def nest[A](f: A => A, v: A, n: Int): A = if (n <= 0) f(v) else nest(f, f(v), n - 1)
}
