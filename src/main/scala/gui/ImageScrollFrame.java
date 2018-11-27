package gui;

import javax.swing.*;
import java.awt.*;

public class ImageScrollFrame extends JInternalFrame {
    private RegionSelectorListener listener;
    public ImageScrollFrame(String filename) {

        setIconifiable(true);
        setResizable(true);
        setClosable(true);
        getAccessibleContext().setAccessibleName("Image Internal Frame");
        getAccessibleContext().setAccessibleDescription("Image internal frame.");

        ImageIcon image = new ImageIcon(filename);
        JLabel imageLabel = new JLabel(image);
        JScrollPane scrollPane = new JScrollPane(imageLabel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        listener = RegionSelectorListener.install(scrollPane);
        add(scrollPane, BorderLayout.CENTER);
        pack();
    }

    public static void main(String[] args)
    {
        new ImageScrollFrame("C:\\Users\\john\\Desktop\\DSCF8487.jpg").setVisible(true);
    }
}
