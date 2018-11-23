package filters

import com.sksamuel.scrimage.{Image, Pixel}
import objects.ImagePixelList

import scala.annotation.tailrec

object OpenCloseFilter {
  def erode(img: Image, ele: StructuralElement): Image =
    img.toPar.map { case (x, y, v) => if (ele.checkElementIsFullyContained(img, (x, y))) Pixel(255, 255, 255, v.alpha) else Pixel(0, 0, 0, v.alpha) }.toImage

  def dilate(img: Image, ele: StructuralElement): Image =
    img.toPar.map { case (x, y, v) => if (ele.checkElementIsPartiallyContained(img, (x, y))) Pixel(255, 255, 255, v.alpha) else Pixel(0, 0, 0, v.alpha) }.toImage

  def erodeInPlace(img: Image, ele: StructuralElement, pt: (Int, Int)): ImagePixelList = {
    // this one is simple, since we only have to check the points surrounding the specified point
    val set = if (ele.checkElementIsFullyContained(img, (pt._1, pt._2))) {
      img.setPixel(pt._1, pt._2, Pixel(255, 255, 255, img.pixel(pt).alpha))
      true
    }
    else {
      img.setPixel(pt._1, pt._2, Pixel(0, 0, 0, img.pixel(pt).alpha))
      false
    }
    ImagePixelList(img, if(set) Vector(pt) else Vector.empty)
  }

  def dilateInPlace(img: Image, ele: StructuralElement, pt: (Int, Int)): ImagePixelList = {
    // here, we have to set the points around as well. So we check all the pixels around the specified pixel and set those as well
    val pxlist = ele.element filter {
      e =>
        if (
          pt._1 + e._1 >= 0 &&
            pt._1 + e._1 < img.width &&
            pt._2 + e._2 >= 0 &&
            pt._2 + e._2 < img.height &&
            img.pixel(pt._1 + e._1, pt._2 + e._2).red > 0
        ) {
          img.setPixel(pt._1, pt._2, Pixel(255, 255, 255, img.pixel(pt).alpha))
          true
        }
        else {
          img.setPixel(pt._1, pt._2, Pixel(0, 0, 0, img.pixel(pt).alpha))
          false
      }
    } map {e => (e._1 + pt._1, e._2 + pt._2)}
    ImagePixelList(img, pxlist)
  }


  @tailrec
  final def nest[A](f: A => A, v: A, n: Int): A = if (n <= 0) f(v) else nest(f, f(v), n - 1)
}
