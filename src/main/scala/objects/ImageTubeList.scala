package objects

import com.sksamuel.scrimage.Image

case class ImageTubeList(img: Image, tubeList: Vector[Tubule]) {
  def mkString: String = tubeList.map(_.length).mkString(",")
}

object ImageTubeList {
  /*
  Makes a CSV file
  Each line represents a tube, and each entry in the line represents the length of the line at a specific frame
   */
  def makeCSVString(vec: Vector[ImageTubeList]): String = {
    vec.map(_.tubeList.map(_.length).toArray).toArray.transpose.map(_.mkString(",")).mkString("\n")
  }
}