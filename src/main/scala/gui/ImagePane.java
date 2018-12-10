package gui;

import com.sksamuel.scrimage.Image;
import javafx.concurrent.Task;
import objects.ImageTrackerOptions;
import objects.ImageTubeList;
import prediction.TubeTracker;
import scala.collection.immutable.Vector;
import scala.collection.immutable.VectorBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

public class ImagePane extends JPanel {

    List<Point> points; //change to duples of points
    BufferedImage displayImage;
    RegionSelectorListener listener;
    Vector<BufferedImage> images;
    Vector<Image> processedImages;
    public Boolean doneProcessing;

    public ImagePane(String imagePath) {
        points = new ArrayList<>(25);
        listener = new RegionSelectorListener(this);
        try {
            if (imagePath!=null) {

                images = TiffStackReader.readStack(new File(imagePath));
                processedImages = TubeTracker.processImages(images, ImageTrackerOptions.getOptions());
                displayImage = images.head();

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //use function from regionSelectorListener with point duples
                listener.mouseClicked(e);
                points.add(e.getPoint());
                repaint();
            }
        });
    }

    public Boolean getDoneProcessing() {
        return doneProcessing;
    }

    public ImagePane() {
        points = new ArrayList<>(25);
    }

    @Override
    public Dimension getPreferredSize() {
        return displayImage == null ? new Dimension(520, 520) : new Dimension(displayImage.getWidth(), displayImage.getHeight());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        if (displayImage != null) {
            g2d.drawImage(displayImage, 0, 0, this);
        }
        g2d.setColor(Color.RED);
        int i=0;
        int j=0;
        for (Point p : points) {
            i++;
            g2d.fillOval(p.x - 3, p.y - 3, 6, 6);
            if (i%2==0) {
                j++;
                g2d.drawString(Integer.toString(j),p.x + 7, p.y + 7);
            }
        }

        g2d.dispose();
    }

    public static Vector<BufferedImage> convertToBufferedImage(Vector<Image> vec) {
        VectorBuilder<BufferedImage> builder = new VectorBuilder<>();
        scala.collection.JavaConverters.asJavaCollection(vec).forEach(
                img -> { builder.$plus$eq(img.awt()); }
        );
        return builder.result();
    }

}
