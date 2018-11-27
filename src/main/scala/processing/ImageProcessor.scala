package processing

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import com.sksamuel.scrimage.Image

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
