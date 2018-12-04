package gui;

import scala.collection.immutable.Vector;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class TiffStackWriter {

    public static void writeTiffStack(Vector<BufferedImage> images, File file) {
        if(!ImageIO.getImageWritersByFormatName("TIFF").hasNext()) {
            ImageIO.scanForPlugins();
        }

        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("TIFF");

        if (!writers.hasNext()) {
            throw new IllegalArgumentException("No writer for: " + "TIFF");
        }

        ImageWriter writer = writers.next();

        try {
            ImageOutputStream output = ImageIO.createImageOutputStream(file);
            try {
                writer.setOutput(output);
                writer.prepareWriteSequence(null);
                for (int i=0; i < images.length(); i++ ) {
                    writer.writeToSequence(new IIOImage(images.apply(i),null,null), null);
                    //System.out.println("Successfully wrote: " + i);
                }
            } finally {
                output.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            // Dispose writer in finally block to avoid memory leaks
            writer.dispose();
        }
    }
}
