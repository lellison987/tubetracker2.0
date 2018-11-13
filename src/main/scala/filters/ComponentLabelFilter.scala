package filters

import com.sksamuel.scrimage.{Image, Pixel, PixelTools}

import scala.collection.mutable
class ComponentLabelFilter(structuralElement: StructuralElement = StructuralElements.structSquare) extends Filter{
  private val recStructEle = structuralElement.element.filterNot(p => p._1 == 0 && p._2 == 0)

  override def apply(img: Image): Image = {
    var fill = 1
    var newimg = img.copy
    val flags = Array.fill(img.width, img.height)(false)
    var done = false
    while(!done){
      newimg.pixels.zipWithIndex.find{case (p, i) =>
        val (x,y) = PixelTools.offsetToCoordinate(i, img.width)
        !flags(x)(y) && p.red > 0
      } match {
        case Some((p, i)) =>
          val (x,y) = PixelTools.offsetToCoordinate(i, img.width)
          newimg = floodConnectedComponents(newimg, flags, x, y, fill)
          fill += 1
        case _ => done = true
      }
    }
    newimg
  }

  /*
  def floodConnectedComponents(img: Image, flags: Array[Array[Boolean]], x: Int, y: Int, v: Int): Image = {
    if(
      x >= 0 &&
      x < img.width &&
      y >= 0 &&
      y < img.height &&
      img.pixel(x,y).red > 0 &&
      !flags(x)(y)
    ) {
      img.setPixel(x, y, Pixel(v, v, v, img.pixel(x, y).alpha))
      flags(x)(y) = true
      recStructEle foreach {
        case (dx, dy) =>
          floodConnectedComponents(img, flags, x+dx, y+dy, v)
      }
    }
    img
  }
  */

  def floodConnectedComponents(img: Image, flags: Array[Array[Boolean]], x: Int, y: Int, v: Int): Image = {
    val queue = new mutable.Queue[(Int, Int)]()
    queue.enqueue((x,y))
    while(queue.nonEmpty){
      val (qx, qy) = queue.dequeue()
      if(
        qx >= 0 &&
          qx < img.width &&
          qy >= 0 &&
          qy < img.height &&
          img.pixel(qx,qy).red > 0 &&
          !flags(qx)(qy)
      ) {
        flags(qx)(qy) = true
        img.setPixel(qx, qy, Pixel(v, v, v, img.pixel(qx, qy).alpha))
        recStructEle foreach {element => queue.enqueue((qx + element._1,qy + element._2))}
      }
    }
    img
  }
}
