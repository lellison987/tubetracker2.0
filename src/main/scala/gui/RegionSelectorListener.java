package gui;

import scala.Tuple2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

public class RegionSelectorListener extends JPanel implements MouseListener {
    final JScrollPane label;
    private Point origin = null;
    private LinkedList<Tuple2<Point, Point>> pointsList = null;

    public RegionSelectorListener(JScrollPane theFrame) {
        this.label = theFrame;
        this.pointsList = new LinkedList<>();
    }

    public static RegionSelectorListener install ( final JScrollPane theFrame)
    {
        final RegionSelectorListener dr = new RegionSelectorListener ( theFrame );
        theFrame.addMouseListener ( dr );
        return dr;
    }

    /**should draw all endpoints when repaint() is called, but does not**/
    public void paintComponent(Graphics g) {
        for (int i=0; i<pointsList.size(); i++) {
            System.out.println(i); //not getting here
            g.setColor(Color.RED);
            g.fillOval(pointsList.get(i)._1.x, pointsList.get(i)._1.y,2,2);
            g.fillOval(pointsList.get(i)._2.x, pointsList.get(i)._2.y,2,2);
        }
    }

    public void mouseClicked(MouseEvent event) {
        if (origin == null) { //If the first corner is not set...

            origin = getAbsolutePoint(event); //set it.
//            drawCircle(origin.x,origin.y);

        } else { //if the first corner is already set...

            //calculate width/height substracting from origin
            Point p = getAbsolutePoint(event);

            //output the results (replace this)
            System.out.println("P1 X is: "+ origin.x);
            System.out.println("P1 Y is: "+ origin.y);
            System.out.println("P2 X is: "+ p.x);
            System.out.println("P2 Y is: "+ p.y);

            pointsList.add(new Tuple2<>(origin, p));
            repaint(); //should theoretically display the two dots by calling the paintComponents function, but does not
            pointsList.forEach(e -> System.out.println(e.toString()));
            

            // set origin
            origin = null;
        }
    }

    private Point getAbsolutePoint(MouseEvent event) {
        Point p = event.getPoint();
        Point delta = label.getViewport().getViewPosition();
        return new Point(p.x + delta.x, p.y + delta.y);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public java.util.List<Tuple2<Point, Point>> getPointsList() {
        return pointsList;
    }

    public void dropLastFromPointsList() {
        pointsList.removeLast();
    }
}