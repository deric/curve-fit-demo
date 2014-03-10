package org.clueminer.curve.fit.splines;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import org.openide.util.lookup.ServiceProvider;

/**
 * @see http://www.cse.unsw.edu.au/~lambert/splines/
 * @author Tim Lambert
 */
@ServiceProvider(service = Spline.class)
public class BSpline implements Spline {

    private static final String name = "BSpline";

    @Override
    public String getName() {
        return name;
    }

    // the basis function for a cubic B spline
    public double b(int i, double t) {
        switch (i) {
            case -2:
                return (((-t + 3) * t - 3) * t + 1) / 6;
            case -1:
                return (((3 * t - 6) * t) * t + 4) / 6;
            case 0:
                return (((-3 * t + 3) * t + 3) * t + 1) / 6;
            case 1:
                return (t * t * t) / 6;
        }
        return 0; //we only get here if an invalid i is specified
    }

    public Point2D.Double[] curvePoints(double[] xpoints, double[] ypoints, int steps) {
        int pts = (xpoints.length - 1) * steps;
        Point2D.Double[] curve = new Point2D.Double[pts];
        curve[0] = point(2, 0, xpoints, ypoints);
        for (int i = 2; i < xpoints.length - 1; i++) {
            for (int j = 1; j <= steps; j++) {
                curve[j] = point(i, j / (double) steps, xpoints, ypoints);
            }
        }
        return curve;
    }

    /**
     * Evaluate an ith point on the B spline
     *
     * @param i
     * @param t
     * @param xpoints
     * @param ypoints
     * @return
     */
    @Override
    public Point2D.Double point(int i, double t, double[] xpoints, double[] ypoints) {
        double px = 0;
        double py = 0;
        for (int j = -2; j <= 1; j++) {
            px += b(j, t) * xpoints[i + j];
            py += b(j, t) * ypoints[i + j];
        }
        return new Point2D.Double(px, py);
    }

}
