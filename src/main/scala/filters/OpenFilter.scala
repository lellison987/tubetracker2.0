package filters

import com.sksamuel.scrimage.Image

class OpenFilter(element: StructuralElement, n: Int = 1) extends OpenCloseFilter with Filter{
 def apply(img: Image): Image = apply(img, n)

 def apply(img: Image, n: Int): Image = nest(dilate(_: Image, element), nest(erode(_: Image, element), img, n), n)


}