package filters

import com.sksamuel.scrimage.Image

class StructuralElement(val element: Vector[(Int, Int)]) {
  def checkElementIsFullyContained(img: Image, px: (Int, Int)): Boolean = {
    element foreach {
      e =>
        if (
          px._1 + e._1 >= 0 &&
            px._1 + e._1 < img.width &&
            px._2 + e._2 >= 0 &&
            px._2 + e._2 < img.height &&
            img.pixel(px._1 + e._1, px._2 + e._2).red == 0
        ) return false
    }
    true
  }

    def checkElementIsPartiallyContained(img: Image, px: (Int, Int)): Boolean = {
      element foreach {
        e =>
          if (
            px._1 + e._1 >= 0 &&
              px._1 + e._1 < img.width &&
              px._2 + e._2 >= 0 &&
              px._2 + e._2 < img.height &&
              img.pixel(px._1 + e._1, px._2 + e._2).red > 0
          ) return true
      }
      false
    }
  }
