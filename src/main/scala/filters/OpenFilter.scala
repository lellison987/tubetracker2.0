package filters

import OpenCloseFilter._
import com.sksamuel.scrimage.Image

class OpenFilter(element: StructuralElement, n: Int = 1) extends Filter{
 def apply(img: Image): Image = apply(img, n)

 def apply(img: Image, n: Int): Image = nest(dilate(_: Image, element), nest(erode(_: Image, element), img, n), n)

}