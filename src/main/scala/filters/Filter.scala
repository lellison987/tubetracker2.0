package filters

import com.sksamuel.scrimage.Image

trait Filter {
  def apply(img: Image): Image
}

object Filter {

  implicit class FilterOps(val img: Image) {
    def applyFilter(f: Filter): Image = f.apply(img)
  }

  def checkPxRange(img: Image, px: (Int, Int)): Boolean = {
    if (
      px._1 >= 0 &&
        px._1 < img.width &&
        px._2 >= 0 &&
        px._2 < img.height
    ) true else false
  }
}