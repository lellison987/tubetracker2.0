package prediction

import java.io.File

import com.sksamuel.scrimage.{Image, filter}
import filters._
import objects.Tubule
import filters.Filter._

/* Notes:
  Auto extension:
  0. threshold with low-ish value to get new tube image and get filled component
  1. in new image, run open/close on points in connected component at same location as the first tube
  2. subtract points in previous tube. Remove those which are no longer in the tube
  3. For each leftover point, use linear predictor to determine whether it should be included in the tube. If it is, add it to the list of points
  4. Re-run linear regression
   */

class LinearTracker(
                     val predictor: LinearPredictor,
                     val thresholdValue: Int,
                     val structuralElement: StructuralElement,
                     val sizeFilter: ComponentSizeFilter,
                     val open: OpenFilter,
                     val close: CloseFilter
                   ) {
  def findCenterPoint(tube: Tubule, range: Int = 3): (Int, Int) = {
    //take a slice from the middle and find the centroid of that
    val sortedPts = tube.points.sorted
    try {
        val pts = sortedPts.slice(tube.points.length / 2 - range, tube.points.length / 2 + range)
        val centroid = pts.foldLeft[(Double, Double)]((0,0)){
          case (accum, pt) =>
            (accum._1 + (pt._1.toDouble / pts.length), accum._2 + (pt._2.toDouble / pts.length))
        }
        (math.round(centroid._1).toInt, math.round(centroid._2).toInt)
    }
    catch {
      case e: ArrayIndexOutOfBoundsException => sortedPts(tube.points.length / 2)
    }
  }

  def findNewTube(tube: Tubule, newimg: Image): Tubule = {
    //threshold
    //apply morphological operations? How to avoid applying over entire image? necessary?
    //TODO: experiment with morphological operations (open, then close) -- maybe on subimage
    val timg = newimg.filter(filter.ThresholdFilter(thresholdValue)).applyFilter(open).applyFilter(close)
    //try a center point first
    timg.output(new File("here.jpg"))
    val (cx, cy) = findCenterPoint(tube)
    val centerpts = sizeFilter.getConnectedComponent(timg, Array.fill(timg.width, timg.height)(false), cx, cy)
    //otherwise try all the points to get one that works
    val newpts = if(centerpts.length < sizeFilter.minSize) {
      val pt = tube.points.find {
        case (x, y) => sizeFilter.getConnectedComponent(timg, Array.fill(timg.width, timg.height)(false), x, y).length >= sizeFilter.minSize
      }
      pt match {
        case Some(p) => sizeFilter.getConnectedComponent(timg, Array.fill(timg.width, timg.height)(false), p._1, p._2)
        case None => Vector.empty
      }
    } else centerpts

    val additionalPoints = Set(newpts:_*).diff(Set(tube.points:_*))
    val newtubepts = (additionalPoints filter (pt => predictor.predict(pt, tube.p1, tube.p2))).toVector ++ tube.points.intersect(newpts)
    val (m, b, p) = LinearRegression.doRegression(newtubepts)
    val newtubeendpts = if (newtubepts.nonEmpty) LinearRegression.getLineEndpoints(newtubepts, (m, b)) else ((0,0), (0,0))
    Tubule(newtubepts, newtubeendpts._1, newtubeendpts._2)
  }
}
