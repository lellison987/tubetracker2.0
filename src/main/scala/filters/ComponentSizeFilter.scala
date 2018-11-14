package filters

import com.sksamuel.scrimage.{Image, Pixel, PixelTools}

import scala.collection.mutable

class ComponentSizeFilter(val minSize: Int, val structuralElement: StructuralElement = StructuralElements.structSquare) extends Filter{
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
          val lst = getConnectedComponent(newimg, flags, x, y)
          if(lst.length >= minSize) {
            lst foreach {case (ex, ey) => newimg.setPixel(ex, ey, Pixel(255, 255, 255, img.pixel(ex, ey).alpha))}
          } else {
            lst foreach {case (ex, ey) => newimg.setPixel(ex, ey, Pixel(0, 0, 0, img.pixel(ex, ey).alpha))}
          }
          fill += 1
        case _ => done = true
      }
    }
    newimg
  }


  def getConnectedComponent(img: Image, flags: Array[Array[Boolean]], x: Int, y: Int): Vector[(Int, Int)] = {
    val queue = new mutable.Queue[(Int, Int)]()
    val components = mutable.ListBuffer[(Int, Int)]()
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
        components.+=((qx, qy))
        recStructEle foreach {element => queue.enqueue((qx + element._1,qy + element._2))}
      }
    }
    components.toVector
  }

  def listConnectedComponents(img: Image): Vector[Vector[(Int, Int)]] = {
    val connComponents = new mutable.ListBuffer[Vector[(Int, Int)]]()
    val flags = Array.fill(img.width, img.height)(false)
    var done = false
    while(!done){
      img.pixels.zipWithIndex.find{case (p, i) =>
        val (x,y) = PixelTools.offsetToCoordinate(i, img.width)
        !flags(x)(y) && p.red > 0
      } match {
        case Some((p, i)) =>
          val (x,y) = PixelTools.offsetToCoordinate(i, img.width)
          val lst = getConnectedComponent(img, flags, x, y)
          if(lst.length >= minSize) connComponents += lst
        case _ => done = true
      }
    }
    connComponents.toVector
  }
}
