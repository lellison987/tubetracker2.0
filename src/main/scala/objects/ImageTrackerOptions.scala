package objects

import java.io.{File, IOException, PrintWriter}

import filters.{StructuralElement, StructuralElements}
import io.circe.parser.decode
import io.circe.generic.auto._
import io.circe.syntax._

case class ImageTrackerOptions(
  alpha: Double = 200.0,
  beta: Double = 100.0,
  pValueLimit: Double = 0.05,
  thresholdValue: Int = 65,
  constrastValue: Option[Double] = None,
  structuralElement: StructuralElement = StructuralElements.structCross,
  minSize: Int = 50
)

object ImageTrackerOptions {
  val defaultOptions = ImageTrackerOptions()

  def getOptions: ImageTrackerOptions = {
    if(new File("config.json").exists()) {
      println("Read configuration file.")
      val json = scala.io.Source.fromFile("config.json").mkString
      decode[ImageTrackerOptions](json).getOrElse(ImageTrackerOptions())
    } else {
      val opt = ImageTrackerOptions()
      try {
        val writer = new PrintWriter(new File("config.json"))
        writer.write(opt.asJson.toString())
        writer.close()
      } catch {
        case ioex: IOException => println("Could not open config file for writing!")
        case e : Exception => e.printStackTrace()
      }
      opt
    }
  }

  def writeToConfigFile(opt: ImageTrackerOptions): Unit = {
    try {
      val writer = new PrintWriter(new File("config.json"))
      writer.write(opt.asJson.toString())
      writer.close()
    } catch {
      case ioex: IOException => println("Could not open config file for writing!")
      case e : Exception => e.printStackTrace()
    }
  }
}