package gui;

import com.sksamuel.scrimage.Image;
import objects.ImageTrackerOptions;
import objects.ImageTubeList;
import prediction.TubeTracker;
import scala.Function1;
import scala.collection.immutable.Vector;
import scala.collection.immutable.VectorBuilder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

//import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

//ignore the missing package. It works.
public class TubeTrackerGUI extends JFrame{
    private javax.swing.JDesktopPane desktop;
    private javax.swing.JMenuItem undoMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JMenuBar mainMenuBar;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenuItem runMenuItem;
    private javax.swing.JMenu exportMenu;
    private javax.swing.JMenuItem dataFileMenuItem;
    private javax.swing.JMenuItem labeledImageMenuItem;
    private javax.swing.JMenuItem processedImagesMenuItem;
    private String imagePath;
    private JFrame frame;
    private ImagePane imagePane;

    public static void main(String[] args) {
        new TubeTrackerGUI();
    }

    public TubeTrackerGUI() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame = new JFrame();
                imagePane = new ImagePane();
                frame.add(imagePane);
                initComponents();
                frame.setJMenuBar(mainMenuBar);
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }

            private void initComponents() {
                desktop = new javax.swing.JDesktopPane();
                mainMenuBar = new javax.swing.JMenuBar();
                fileMenu = new javax.swing.JMenu();
                openMenuItem = new javax.swing.JMenuItem();
                runMenuItem = new javax.swing.JMenuItem();
                exportMenu = new javax.swing.JMenu();
                dataFileMenuItem = new javax.swing.JMenuItem();
                labeledImageMenuItem = new javax.swing.JMenuItem();
                processedImagesMenuItem = new javax.swing.JMenuItem();
                jSeparator1 = new javax.swing.JSeparator();
                undoMenuItem = new javax.swing.JMenuItem();
                desktop.setVisible(true);

                setTitle("Image Viewer");
                addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent evt) {
                        exitForm(evt);
                    }
                });

                getAccessibleContext().setAccessibleName("Image Viewer Frame");
                getContentPane().add(desktop, java.awt.BorderLayout.CENTER);
                desktop.getAccessibleContext().setAccessibleName("Image Desktop");
                desktop.getAccessibleContext().setAccessibleDescription("Image desktop");

                fileMenu.setMnemonic('f');
                fileMenu.setText("File");
                openMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
                openMenuItem.setMnemonic('o');
                openMenuItem.setText("Open");
                openMenuItem.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        openMenuItemActionPerformed(evt);
                    }
                });

                fileMenu.add(openMenuItem);
                openMenuItem.getAccessibleContext().setAccessibleName("Open Menu Item");
                openMenuItem.getAccessibleContext().setAccessibleDescription("Open menu item.");

                runMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
                runMenuItem.setMnemonic('r');
                runMenuItem.setText("Run");
                runMenuItem.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        runMenuItemActionPerformed(evt);
                    }
                });

                fileMenu.add(runMenuItem);
                runMenuItem.getAccessibleContext().setAccessibleName("Run Menu Item");
                runMenuItem.getAccessibleContext().setAccessibleDescription("Run menu item.");

                undoMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
                undoMenuItem.setMnemonic('z');
                undoMenuItem.setText("Undo");
                undoMenuItem.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        undoMenuItemActionPerformed(evt);
                    }
                });

                fileMenu.add(undoMenuItem);
                undoMenuItem.getAccessibleContext().setAccessibleName("Undo Menu Item");
                undoMenuItem.getAccessibleContext().setAccessibleDescription("Undo menu item.");

                fileMenu.add(jSeparator1);

                mainMenuBar.add(fileMenu);
                fileMenu.getAccessibleContext().setAccessibleName("File Menu");
                fileMenu.getAccessibleContext().setAccessibleDescription("File menu.");

                exportMenu.setMnemonic('e');
                exportMenu.setText("Export");
                dataFileMenuItem.setText("Data File");
                dataFileMenuItem.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        exportDataFileActionPerformed(evt);
                    }
                });
                exportMenu.add(dataFileMenuItem);
                dataFileMenuItem.getAccessibleContext().setAccessibleName("Export File Menu Item");
                dataFileMenuItem.getAccessibleContext().setAccessibleDescription("Export file menu item.");

                labeledImageMenuItem.setText("Labeled Image");
                labeledImageMenuItem.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        exportLabeledImagesActionPerformed(evt);
                    }
                });
                exportMenu.add(labeledImageMenuItem);
                labeledImageMenuItem.getAccessibleContext().setAccessibleName("Export Labeled Image Menu Item");
                labeledImageMenuItem.getAccessibleContext().setAccessibleDescription("Export labeled image menu item");

                processedImagesMenuItem.setText("Processed Images");
                processedImagesMenuItem.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        exportProcessedImagesActionPerformed(evt);
                    }
                });
                exportMenu.add(processedImagesMenuItem);
                processedImagesMenuItem.getAccessibleContext().setAccessibleName("Export Processed Images Menu Item");
                processedImagesMenuItem.getAccessibleContext().setAccessibleDescription("Export processed images menu item");

                mainMenuBar.add(exportMenu);
                exportMenu.getAccessibleContext().setAccessibleName("Export Menu");
                exportMenu.getAccessibleContext().setAccessibleDescription("Export menu.");

                setJMenuBar(mainMenuBar);
                mainMenuBar.getAccessibleContext().setAccessibleName("TubeTrackerGUI Menu Bar");
                mainMenuBar.getAccessibleContext().setAccessibleDescription("TubeTrackerGUI menu bar.");

            }
        });


    }

    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }



    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
        chooser.addChoosableFileFilter(new ImageViewer.ImageFileFilter());
        int option = chooser.showOpenDialog(this);
        if (option == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File file = chooser.getSelectedFile();
            if (file == null) return;
            imagePath = file.getAbsolutePath();
            frame.remove(imagePane);
            frame.setVisible(false);
            imagePane = new ImagePane();
            frame.add(imagePane);
            frame.setVisible(true);
        }
    }

    private void runMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        Vector<ImageTubeList> trackedTubes = prediction.TubeTracker.trackFromProcessedImagesVector(imagePane.processedImages, imagePane.listener.getPointsList(), ImageTrackerOptions.getOptions());
        System.out.println(ImageTubeList.makeCSVString(trackedTubes));
    }

    private void undoMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        if(imagePane.listener.completedPointPair()) {
            //point pair was completed, must remove last two points
            imagePane.listener.reset();
            imagePane.listener.dropLastFromPointsList();
            //remove last point
            if(imagePane.points.size() > 0) imagePane.points.remove(imagePane.points.size() - 1);
            if(imagePane.points.size() > 0) imagePane.points.remove(imagePane.points.size() - 1);
            //redraw
            imagePane.repaint();
        } else {
            //point pair was not completed and origin != null
            imagePane.listener.reset();
            //remove last point
            if(imagePane.points.size() > 0) imagePane.points.remove(imagePane.points.size() - 1);
            //redraw
            imagePane.repaint();
        }
    }

    private void exportDataFileActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("Export Data File");
    }

    private void exportLabeledImagesActionPerformed(java.awt.event.ActionEvent evt) {
        System.out.println("Export Labeled Images");
    }

    private void exportProcessedImagesActionPerformed(java.awt.event.ActionEvent evt) {
        javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
        chooser.addChoosableFileFilter(new ImageViewer.ImageFileFilter());
        int option = chooser.showSaveDialog(this);
        if (option == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File file = chooser.getSelectedFile();
            String f = file.getAbsolutePath();
            TiffStackWriter.writeTiffStack(this.imagePane.biProcessedImages, new File(f));
        }
    }

    public class ImagePane extends JPanel {

        private List<Point> points; //change to duples of points
        private BufferedImage displayImage;
        private RegionSelectorListener listener;
        private Vector<BufferedImage> images;
        private Vector<Image> processedImages;
        private Vector<BufferedImage> biProcessedImages;

        public ImagePane() {
            points = new ArrayList<>(25);
            listener = new RegionSelectorListener(this);
            try {
                if (imagePath!=null) {
                    System.out.println("Reading images...");
                    images = TiffStackReader.readStack(new File(imagePath));
                    System.out.println("Processing images... Please wait.");
                    processedImages = TubeTracker.processImages(images);
                    biProcessedImages = convertToBufferedImage(processedImages);
                    System.out.println("Done processing images.");
                    displayImage = images.head();
                    //test of image writing
                    TiffStackWriter.writeTiffStack(biProcessedImages, new File("test.tif"));
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
            for (Point p : points) {
                g2d.fillOval(p.x - 3, p.y - 3, 6, 6);
            }
            g2d.dispose();
        }

        private Vector<BufferedImage> convertToBufferedImage(Vector<Image> vec) {
            VectorBuilder<BufferedImage> builder = new VectorBuilder<>();
            scala.collection.JavaConverters.asJavaCollection(vec).forEach(
                    img -> { builder.$plus$eq(img.awt()); }
            );
            return builder.result();
        }

    }

}