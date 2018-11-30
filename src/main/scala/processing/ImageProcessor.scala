package processing

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import com.sksamuel.scrimage.{Image, filter}
import filters.Kernels.{kernBlur, kernSharpen}
import filters.{CloseFilter, OpenFilter, StructuralElements}
import filters.Kernel._
import filters.Kernels._
import filters._
import filters.Filter._
import scala.concurrent.duration._

import scala.concurrent.{ExecutionContext, Future}

class ImageProcessor(val morphism: Image => Image)(implicit val ec: ExecutionContext, val ac: ActorSystem, val tm: Timeout) {
  def processImages(images: Vector[Image]): Future[Vector[Image]] = {
    val futVec = images map {
      img =>
        val actor = ac.actorOf(ImageProcessingActor.props(morphism))
        val res = actor ? img
        res.mapTo[Image]
    }
    Future.sequence(futVec)
  }
}

object ImageProcessor {
  def getDefaultImageProcesser(implicit ec: ExecutionContext, system: ActorSystem, timeout: Timeout): ImageProcessor = {
    val open = new OpenFilter(StructuralElements.structCross, 1)
    val close = new CloseFilter(StructuralElements.structCross, 1)
    implicit val ec: ExecutionContext = ExecutionContext.global
    implicit val system: ActorSystem = ActorSystem("TubeTrackerSystem")
    implicit val timeout: Timeout = Timeout(24.hours)
    new ImageProcessor(img => img
      .applyKernel(kernBlur)
      .applyKernel(kernSharpen)
      .filter(filter.GrayscaleFilter)
      .filter(filter.ThresholdFilter(75))
      .applyFilter(open)
      .applyFilter(close))
  }
}
