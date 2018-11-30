package gui

import java.awt.image.BufferedImage
import java.io.File

import javax.imageio.ImageIO

object TiffStackReader {
  def readStack(file: File): Vector[BufferedImage] = {
    /* taken from */
    // Create input stream
    val input = ImageIO.createImageInputStream(file)

    try {
      // Get the reader
      val readers = ImageIO.getImageReaders(input)

      if (!readers.hasNext) {
        throw new IllegalArgumentException("No reader for: " + file)
      }

      val reader = readers.next()

      try {
        reader.setInput(input)
        val param = reader.getDefaultReadParam

        //here is return
        (for (i <- 0 until reader.getNumImages(true)) yield reader.read(i, param)).toVector

      }
      finally {
        // Dispose reader in finally block to avoid memory leaks
        reader.dispose()
      }
    }
    finally {
      // Close stream in finally block to avoid resource leaks
      input.close()
    }
  }

  def main(args: Array[String]): Unit = {
    val imgs =
      readStack(
        new File(
          "C:\\Users\\john\\Documents\\School\\Computer Science\\Geometric Computing for Biomedicine\\Project\\microtubule_videos\\MT dynamics example image stack.tif"
        )
      )
    println(s"Read ${imgs.length} images")
  }
}
