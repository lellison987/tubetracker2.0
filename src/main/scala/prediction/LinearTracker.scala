package prediction

import java.io.File

import com.sksamuel.scrimage.{Image, Pixel, filter}
import filters._
import objects.Tubule
import filters.Filter._

import scala.collection.mutable

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
                     val sizeFilter: ComponentSizeFilter
                   ) {
  def findCenterPoint(tubepts: Vector[(Int, Int)], range: Int = 3): (Int, Int) = {
    //take a slice from the middle and find the centroid of that
    val sortedPts = tubepts.sorted
    try {
        val pts = sortedPts.slice(tubepts.length / 2 - range, tubepts.length / 2 + range)
        val centroid = pts.foldLeft[(Double, Double)]((0,0)){
          case (accum, pt) =>
            (accum._1 + (pt._1.toDouble / pts.length), accum._2 + (pt._2.toDouble / pts.length))
        }
        (math.round(centroid._1).toInt, math.round(centroid._2).toInt)
    }
    catch {
      case e: ArrayIndexOutOfBoundsException => sortedPts(tubepts.length / 2)
    }
  }

  def findNewTube(tube: Tubule, newimg: Image): Tubule = {
    //threshold
    //apply morphological operations? How to avoid applying over entire image? necessary?
    //TODO: experiment with morphological operations (open, then close) -- maybe on subimage
    val timg = newimg
    val pts = tube.points
    //try a center point first
    //timg.output(new File("here.jpg"))
    val (cx, cy) = findCenterPoint(pts)
    val centerpts = sizeFilter.getConnectedComponent(timg, Array.fill(timg.width, timg.height)(false), cx, cy)
    //otherwise try all the points to get one that works
    val newpts = if(centerpts.length < sizeFilter.minSize) {
      val pt = tube.points.find {
        case (x, y) => sizeFilter.getConnectedComponent(timg, Array.fill(timg.width, timg.height)(false), x, y).length >= sizeFilter.minSize
      }
      pt match {
        case Some(cp) => sizeFilter.getConnectedComponent(timg, Array.fill(timg.width, timg.height)(false), cp._1, cp._2)
        case None => Vector.empty
      }
    } else centerpts

    val additionalPoints = Set(newpts:_*).diff(Set(tube.points:_*))
    val newtubepts = (additionalPoints filter (pt => predictor.predict(pt, tube.p1, tube.p2))).toVector ++ tube.points.intersect(newpts)
    val (m, b, p) = LinearRegression.doRegression(newtubepts)
    val newtubeendpts = if (newtubepts.nonEmpty) LinearRegression.getLineEndpoints(newtubepts, (m, b)) else ((0,0), (0,0))
    Tubule(newtubepts, newtubeendpts._1, newtubeendpts._2)
  }


  /*
  def findNewTubeFromPoints(tube: Tubule, newimg: Image): Tubule = {
    //threshold
    //apply morphological operations? How to avoid applying over entire image? necessary?
    //TODO: experiment with morphological operations (open, then close) -- maybe on subimage
    //val timg = newimg.filter(filter.ThresholdFilter(thresholdValue)).applyFilter(open).applyFilter(close)
    val timg = newimg
    val slope = (tube.p2._2 - tube.p1._2).toDouble / (tube.p2._1 - tube.p1._1)
    val intercept = tube.p1._2 - (tube.p1._1 * slope)
    //try a center point first
    //timg.output(new File("here.jpg"))
    val pts = for{
      x <- math.min(tube.p2._1, tube.p1._1) to math.max(tube.p2._1, tube.p1._1)
    } yield (x, math.round(x*slope + intercept).toInt)
    //otherwise try all the points to get one that works
    val newptsSet = mutable.Set[(Int, Int)]()
    pts foreach {
      pt =>
        val conn = sizeFilter.getConnectedComponent(timg, Array.fill(timg.width, timg.height)(false), pt._1, pt._2)
        if(conn.length >= sizeFilter.minSize) newptsSet ++= conn
    }
    val newpts = newptsSet.toVector
    val additionalPoints = Set(newpts:_*).diff(Set(tube.points:_*))
    val newtubepts = (additionalPoints filter (pt => predictor.predict(pt, tube.p1, tube.p2))).toVector ++ tube.points.intersect(newpts)
    val (m, b, p) = LinearRegression.doRegression(newtubepts)
    val newtubeendpts = if (newtubepts.nonEmpty) LinearRegression.getLineEndpoints(newtubepts, (m, b)) else ((0,0), (0,0))
    Tubule(newtubepts, newtubeendpts._1, newtubeendpts._2)
  }
  */

  def findNewTubeFromPoints(tube: Tubule, newimg: Image): Tubule = {
    //threshold
    //apply morphological operations? How to avoid applying over entire image? necessary?
    //TODO: experiment with morphological operations (open, then close) -- maybe on subimage
    //val timg = newimg.filter(filter.ThresholdFilter(thresholdValue)).applyFilter(open).applyFilter(close)
    val timg = newimg
    val slope = (tube.p2._2 - tube.p1._2).toDouble / (tube.p2._1 - tube.p1._1)
    val intercept = tube.p1._2 - (tube.p1._1 * slope)
    //try a center point first
    //timg.output(new File("here.jpg"))
    val pts = for{
      x <- math.min(tube.p2._1, tube.p1._1) to math.max(tube.p2._1, tube.p1._1)
    } yield (x, math.round(x*slope + intercept).toInt)
    //otherwise try all the points to get one that works
    var newptsSet = mutable.Set[(Int, Int)]()
    pts foreach {
      pt =>
        if(!newptsSet.contains(pt)) {
          val conn = sizeFilter.getConnectedComponent(timg, Array.fill(timg.width, timg.height)(false), pt._1, pt._2)
          if(conn.length >= sizeFilter.minSize) newptsSet ++= conn
        }
    }
    val newpts = newptsSet.toVector
    val additionalPoints = Set(newpts:_*).diff(Set(tube.points:_*))
    val newtubepts = (additionalPoints filter (pt => predictor.predict(pt, tube.p1, tube.p2))).toVector ++
      (if(tube.points.isEmpty) newpts else tube.points.intersect(newpts))
    val res = getLargestConnectedComponent(timg, newtubepts)
    // WHYYYYYYY is the first one smaller?
    // First one only has pts at ENDS because there are no points in the vector
    // so if the vector of points is empty just add everything in the set
    //LIMITATION of this method: does not deal well with overlapping tubes :(
    val (m, b, p) = LinearRegression.doRegression(res)
    val newtubeendpts = if (newtubepts.nonEmpty) LinearRegression.getLineEndpoints(res, (m, b)) else ((0,0), (0,0))
    Tubule(res, newtubeendpts._1, newtubeendpts._2)
  }

  private def getLargestConnectedComponent(img: Image, pts: Vector[(Int, Int)]): Vector[(Int, Int)] = {
    val minX = pts.map(_._1).min
    val maxX = pts.map(_._1).max
    val minY = pts.map(_._2).min
    val maxY = pts.map(_._2).max
    val subimg = img.subimage(minX, minY, maxX - minX + 1, maxY - minY + 1).blank
    pts foreach {
      case (x, y) => subimg.setPixel(x - minX, y - minY, Pixel(255,255,255,0))
    }
    val comps = sizeFilter.listConnectedComponents(subimg, 1)
    val maxcomp = if(comps.nonEmpty) comps.maxBy(_.length) else Vector.empty
    maxcomp map (px => (px._1 + minX, px._2 + minY))
  }
}
