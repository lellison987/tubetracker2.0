import java.io.File

import akka.actor.ActorSystem
import akka.util.Timeout
import com.sksamuel.scrimage.{Image, Pixel, filter}
import filters.Kernel._
import filters.Kernels._
import filters._
import filters.Filter._
import objects.{ImageTubeList, Tubule}
import prediction.{ImageTracker, LinearPredictor, LinearRegression, LinearTracker}
import processing.ImageProcessor

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration._

object TubeTracker extends App {


  /*

  TODO:
  Check catastrophe
  Output lengths
  Set up actor system to preprocess images
   */

  val open = new OpenFilter(StructuralElements.structCross, 1)
  val close = new CloseFilter(StructuralElements.structCross, 1)
  implicit val ec: ExecutionContext = ExecutionContext.global
  implicit val system: ActorSystem = ActorSystem("TubeTrackerSystem")
  implicit val timeout: Timeout = Timeout(2.hours)
  val proc = new ImageProcessor(img => img
    .applyKernel(kernBlur)
    .applyKernel(kernSharpen)
    .filter(filter.GrayscaleFilter)
    .filter(filter.ThresholdFilter(75))
    .applyFilter(open)
    .applyFilter(close))
  val fut = proc.processImages(({1 to 10} map {i => Image.fromFile(new File(s"mt$i.jpg")) }).toVector)
  val images = Await.result(fut, Duration.Inf)
//  images.zipWithIndex foreach {
//    case (img, idx) =>
//      img.output(new File(s"test${idx+1}.jpg"))
//  }
  println("Done processing images.")
  val pred = new LinearPredictor()
  val size = new ComponentSizeFilter(50)
  val tracker = new LinearTracker(pred, 65, StructuralElements.structCross, size)
  val imgTracker = new ImageTracker(images.tail, ImageTracker.getTubes(images.head, size), tracker)
  val tubesList = imgTracker.trackTubes()
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
}

