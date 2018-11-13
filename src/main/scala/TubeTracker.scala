import java.io.File

import com.sksamuel.scrimage.{Image, Pixel, filter}
import filters.Kernel._
import filters.Kernels._
import filters._
import filters.Filter._
import prediction.LinearRegression

object TubeTracker extends App {


  /*

  TODO:
  Automatic contrast
  Interpolation of tube from two points
  prediction of extension
   */


  val file = new File(args(0))
  val outFile = new File(args(1))
  val outFile2 = new File("skel_" + args(1))
  val img: Image = Image.fromFile(file)
    .applyKernel(kernBlur)
    .applyKernel(kernSharpen)
    .filter(filter.GrayscaleFilter)
  //.filter(filter.ContrastFilter(0.5))
  val skel = new SkeletonizeFilter()
  val open = new OpenFilter(StructuralElements.structCross, 1)
  val close = new CloseFilter(StructuralElements.structCross, 1)
  val size = new ComponentSizeFilter(50)
  println("Read image")
  val mapped: Image =
    img
      .filter(filter.ThresholdFilter(60))
      .applyFilter(size)
      .applyFilter(open)
      .applyFilter(close)
  //.applyFilter(skel)
  println("Mapped image")
  size.listConnectedComponents(mapped) foreach { pts =>
    val mbp = LinearRegression.doRegression(pts)
    println(s"Modelled tube with significance ${mbp._3}")
    val regpts = LinearRegression.produceConstrainedLinePoints(pts, mbp)
    regpts foreach {
      case (x, y) => if(Filter.checkPxRange(mapped, (x,y))) {img.setPixel(x, y, Pixel(255, 0, 0, mapped.pixel(x, y).alpha));
        mapped.setPixel(x, y, Pixel(255, 0, 0, mapped.pixel(x, y).alpha))}
    }
  }
  img.output(outFile)
  mapped.output(outFile2)
  println("Wrote image")
}

