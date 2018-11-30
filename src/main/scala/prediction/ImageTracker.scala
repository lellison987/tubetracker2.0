package prediction

import com.sksamuel.scrimage.Image
import filters.{ComponentSizeFilter, Filter}
import objects.{ImageTrackerOptions, ImageTubeList, Tubule}

//images should be preprocessed
//first image should be the first one for which tubes should be predicted -- e.g allimages.tail
class ImageTracker(val images: Vector[Image], private var tubes: Vector[Tubule], val tracker: LinearTracker) {
  def trackTubes(): Vector[ImageTubeList] =
    images map {
      img => {
        val newtubes = tubes map { tracker.findNewTubeFromPoints(_, img) }
        tubes = newtubes
        ImageTubeList(img, newtubes)
      }
    }
}

object ImageTracker {
  def getTubes(img: Image, size: ComponentSizeFilter): Vector[Tubule] = {
    val tubes = size.listConnectedComponents(img) map { pts =>
      val mbp = LinearRegression.doRegression(pts)
      //println(s"Modelled tube with significance ${mbp._3}")
      val regpts = LinearRegression.produceConstrainedLinePoints(pts, mbp)
      Tubule(pts, regpts.head, regpts.last)
    }
    tubes
  }

  def getLinearTracker(opt: ImageTrackerOptions): LinearTracker = {
    new LinearTracker(
      new LinearPredictor(opt.alpha, opt.beta, opt.pValueLimit),
      opt.thresholdValue,
      opt.structuralElement,
      new ComponentSizeFilter(opt.minSize)
    )
  }
}
