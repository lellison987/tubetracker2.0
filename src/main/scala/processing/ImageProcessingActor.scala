package processing

import akka.actor.{Actor, ActorSystem, Props}
import com.sksamuel.scrimage.Image

class ImageProcessingActor(morphism: Image => Image) extends Actor{
  override def receive: Receive = {
    case img: Image =>
      sender ! morphism(img)
    case _ => throw new UnsupportedOperationException("Cannot process object")
  }
}

object ImageProcessingActor {
  def props(morphism: Image => Image): Props =
    Props(new ImageProcessingActor(morphism))
}