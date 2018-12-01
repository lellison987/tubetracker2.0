package prediction

import java.awt.Point
import java.awt.image.BufferedImage

import akka.actor.ActorSystem
import akka.util.Timeout
import com.sksamuel.scrimage.Image
import objects.{ImageTrackerOptions, ImageTubeList, Tubule}
import processing.ImageProcessor

import scala.concurrent.duration._
import scala.collection.JavaConverters._
import scala.concurrent.{Await, ExecutionContext}

object TubeTracker {

  implicit val ec: ExecutionContext = ExecutionContext.global
  implicit val system: ActorSystem = ActorSystem("TubeTrackerSystem")
  implicit val timeout: Timeout = Timeout(24.hours)

  implicit def pointToTuple(p: Point): (Int, Int) = (p.x, p.y)

  def track(imgs: Vector[BufferedImage], tubePts: Vector[(Point, Point)], opt: ImageTrackerOptions): Vector[ImageTubeList] = {
    try {
      val proc = ImageProcessor.getDefaultImageProcesser
      val fut = proc.processImages(imgs map { i => Image.fromAwt(i) })
      val images = Await.result(fut, Duration.Inf)
      println("Done processing images.")
      val imgTracker = new ImageTracker(images.tail, tubePts map { t => Tubule(Vector.empty, t._1, t._2) }, ImageTracker.getLinearTracker(opt))
      imgTracker.trackTubes()
    } finally system.terminate()
  }

  def track(imgs: Vector[BufferedImage], tubePts: java.util.List[(Point, Point)], opt: ImageTrackerOptions = ImageTrackerOptions()): Vector[ImageTubeList] = {
    try {
      val proc = ImageProcessor.getDefaultImageProcesser
      val fut = proc.processImages(imgs map { i => Image.fromAwt(i) })
      val images = Await.result(fut, Duration.Inf)
      println("Done processing images.")
      val imgTracker = new ImageTracker(images.tail, tubePts.asScala.toVector map { t => Tubule(Vector.empty, t._1, t._2) }, ImageTracker.getLinearTracker(opt))
      imgTracker.trackTubes()
    } finally system.terminate()
  }
  /*

  TODO:
  Check catastrophe
  Output lengths
   */
//  images.zipWithIndex foreach {
//    case (img, idx) =>
//      img.output(new File(s"test${idx+1}.jpg"))
//  }

  /*
  tubesList.zipWithIndex foreach { case (ImageTubeList(img, tubes), idx) =>
    tubes filter {tube => tube.points.nonEmpty} foreach { tube =>
      val mbp = LinearRegression.doRegression(tube.points)
      println(s"Modelled tube with significance ${mbp._3} from ${tube.p1} to ${tube.p2} and length ${math.sqrt(math.pow(tube.p1._2 - tube.p2._2, 2) + math.pow(tube.p1._1 - tube.p2._1, 2))}")
      val regpts = LinearRegression.produceConstrainedLinePoints(tube.points, mbp)
      regpts foreach {
        case (x, y) => if (Filter.checkPxRange(img, (x, y))) {
          img.setPixel(x, y, Pixel(255, 0, 0, img.pixel(x, y).alpha))
        }
      }
    }
    img.output(new File(s"test${idx+1}.jpg"))
  }
  system.terminate()
  */
}

