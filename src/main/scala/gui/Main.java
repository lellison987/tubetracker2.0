import gui.ImageScrollFrame;
import gui.ImageViewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

//import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

//ignore the missing package. It works.
public class Main extends JFrame{
    private javax.swing.JDesktopPane desktop;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JMenuBar mainMenuBar;
    private javax.swing.JMenuItem openMenuItem;
    private String imagePath;
    private JFrame frame;
    private TestPane imagePane;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame = new JFrame();
                imagePane = new TestPane();
                frame.add(imagePane);
                initComponents();
                frame.setJMenuBar(mainMenuBar);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }

            private void initComponents() {
                desktop = new javax.swing.JDesktopPane();
                mainMenuBar = new javax.swing.JMenuBar();
                fileMenu = new javax.swing.JMenu();
                openMenuItem = new javax.swing.JMenuItem();
                jSeparator1 = new javax.swing.JSeparator();
                exitMenuItem = new javax.swing.JMenuItem();
                JButton b1 = new javax.swing.JButton("Hello");
                b1.setSize(100,25);
                b1.setVisible(true);
                desktop.add(b1);
                desktop.setVisible(true);

                b1.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        System.out.println("Hello");
                    }
                });

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

                fileMenu.add(jSeparator1);

//                exitMenuItem.setMnemonic('x');
//                exitMenuItem.setText("Exit");
//                exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
//                    public void actionPerformed(java.awt.event.ActionEvent evt) {
//                        exitMenuItemActionPerformed(evt);
//                    }
//                });
//
//                fileMenu.add(exitMenuItem);
//                exitMenuItem.getAccessibleContext().setAccessibleName("Exit Menu Item");
//                exitMenuItem.getAccessibleContext().setAccessibleDescription("Exit menu item.");

                mainMenuBar.add(fileMenu);
                fileMenu.getAccessibleContext().setAccessibleName("File Menu");
                fileMenu.getAccessibleContext().setAccessibleDescription("File menu.");

                setJMenuBar(mainMenuBar);
                mainMenuBar.getAccessibleContext().setAccessibleName("Main Menu Bar");
                mainMenuBar.getAccessibleContext().setAccessibleDescription("Main menu bar.");

            }
        });


    }

    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }

//    private void exitMenuItemActionPerformed(java.awt.event.WindowEvent evt) {
//        System.exit(0);
//    }

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
            imagePane = new TestPane();
            frame.add(imagePane);
            frame.setVisible(true);

//            ImageScrollFrame ifr = new ImageScrollFrame(imagePath);
//            desktop.add(ifr, javax.swing.JLayeredPane.DEFAULT_LAYER);
//            setContentPane(ifr);
//            ifr.setVisible( true );
//            ifr.setSize(530, 550);
//            ifr.setLocation(100, 100);
//            desktop.setSelectedFrame(ifr);

        }
    }

    public class TestPane extends JPanel {

        private List<Point> points; //change to duples of points
        private BufferedImage image;

        public TestPane() {
            points = new ArrayList<>(25);
            try {
                if (imagePath!=null) {
                    image = ImageIO.read(new File(imagePath));
                } else {
                    image = ImageIO.read(new File("C:\\Users\\Lauren\\IdeaProjects\\tubetracker2.0\\mt1.jpg"));
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    //use function from regionSelectorListener with point duples
                    points.add(e.getPoint());
                    repaint();
                }
            });
        }

        @Override
        public Dimension getPreferredSize() {
            return image == null ? new Dimension(200, 200) : new Dimension(image.getWidth(), image.getHeight());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            if (image != null) {
                g2d.drawImage(image, 0, 0, this);
            }
            g2d.setColor(Color.RED);
            //change to use point duples not points
            for (Point p : points) {
                g2d.fillOval(p.x - 4, p.y - 4, 8, 8);
            }
            g2d.dispose();
        }

    }

}