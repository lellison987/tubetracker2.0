package filters

import com.sksamuel.scrimage.Image

class CloseFilter(element: StructuralElement, n: Int = 1) extends OpenCloseFilter with Filter{
  def apply(img: Image): Image = apply(img, n)

  def apply(img: Image, n: Int): Image = nest(erode(_: Image, element), nest(dilate(_: Image, element), img, n), n)

}
