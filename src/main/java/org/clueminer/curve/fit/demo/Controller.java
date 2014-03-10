package org.clueminer.curve.fit.demo;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import org.clueminer.curve.fit.splines.Bezier;
import org.clueminer.curve.fit.splines.Spline;

/**
 *
 * @author Tomas Barton
 */
public class Controller implements CanvasListener {

    private final CurvePanel panel;
    protected Spline model = new Bezier();
    private Polygon pts;
    private static final int EPSILON = 36;  /* square of distance for picking */

    private static final Font f = new Font("Courier", Font.PLAIN, 12);
    private int selection = -1;
    private final int dotWidth = 4;
    private final int dotHeight = 4;
    private BufferedImage bufferedImage;
    private Graphics2D buff;
    private final int STEPS = 12;

    public Controller(CurvePanel panel) {
        this.panel = panel;
        init();
    }

    private void init() {
        panel.addListener(this);
        pts = new Polygon();
        bufferedImage = createBufferedImage(Color.BLUE);
    }

    private BufferedImage createBufferedImage(Color color) {
        bufferedImage = new BufferedImage(dotWidth, dotHeight, BufferedImage.TYPE_INT_ARGB);
        buff = bufferedImage.createGraphics();
        Ellipse2D.Double ellipse = new Ellipse2D.Double(0, 0, dotWidth, dotHeight);
        buff.setPaint(color);
        buff.draw(ellipse);
        buff.fill(ellipse);
        return bufferedImage;
    }

    /**
     * Removes all elements form the canvas
     */
    public void clear() {
        panel.repaint();
        //model.reset();
        pts = new Polygon();
    }

    public void setSpline(Spline spline) {
        this.model = spline;
    }

    /**
     * return index of control point near to (x,y) or -1 if nothing near
     *
     * @param x
     * @param y
     * @return
     */
    @Override
    public int findPoint(int x, int y) {
        int mind = Integer.MAX_VALUE;
        selection = -1;
        for (int i = 0; i < pts.npoints; i++) {
            int d = pow(pts.xpoints[i] - x) + pow(pts.ypoints[i] - y);
            if (d < mind && d < EPSILON) {
                mind = d;
                selection = i;
            }
        }
        return selection;
    }

    // square of an int
    public static int pow(int x) {
        return x * x;
    }

    /**
     * add a control point, return index of new control point
     *
     * @param x
     * @param y
     * @return index of added point
     */
    @Override
    public int addPoint(int x, int y) {
        pts.addPoint(x, y);
        return selection = pts.npoints - 1;
    }

    /**
     * set selected control point
     *
     * @param x
     * @param y
     */
    @Override
    public void setPoint(int x, int y) {
        if (selection >= 0) {
            pts.xpoints[selection] = x;
            pts.ypoints[selection] = y;
        }
    }

    /**
     * remove selected control point
     */
    @Override
    public void removePoint() {
        if (selection >= 0) {
            pts.npoints--;
            for (int i = selection; i < pts.npoints; i++) {
                pts.xpoints[i] = pts.xpoints[i + 1];
                pts.ypoints[i] = pts.ypoints[i + 1];
            }
        }
    }

    /**
     * paint this curve into g.
     *
     * @param g
     */
    @Override
    public void paint(Graphics2D g) {
        AffineTransform at = new AffineTransform();
        at.scale(1, 1);
        FontMetrics fm = g.getFontMetrics(f);
        g.setFont(f);
        int h = fm.getAscent() / 2;

        for (int i = 0; i < pts.npoints; i++) {
            String s = Integer.toString(i);
            int w = fm.stringWidth(s) / 2 - 4;
            g.drawString(Integer.toString(i), pts.xpoints[i] - w, pts.ypoints[i] + h);

            //filling shapes seems to be very expensive operation
            //this is probably the fastest way how to draw
            at.setToIdentity();
            at.translate(pts.xpoints[i] - 2, pts.ypoints[i] - 2);
            g.drawImage(bufferedImage, at, null);
        }

        drawLine(g);
    }

    private void drawLine(Graphics2D g) {
        Polygon pol = new Polygon();
        double[] xp = Doubles.toArray(Ints.asList(pts.xpoints));
        double[] yp = Doubles.toArray(Ints.asList(pts.xpoints));

        Point2D.Double[] points = model.curvePoints(xp, yp, STEPS);
        if (points.length > 0) {
            System.out.println("len: " + points.length);
            int i = 0;
            for (Point2D.Double point : points) {
                System.out.println((i++) + " point: " + point);
                if (point != null) {
                    pol.addPoint(Math.round((float) point.getX()), Math.round((float) point.getY()));
                }
            }

            g.drawPolyline(pol.xpoints, pol.ypoints, pol.npoints);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < pts.npoints; i++) {
            result.append(" ").append(pts.xpoints[i]).append(" ").append(pts.ypoints[i]);
        }
        return result.toString();
    }

}
