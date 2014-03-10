package org.clueminer.curve.fit.splines;

import java.awt.geom.Point2D;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @see http://www.cse.unsw.edu.au/~lambert/splines/
 * @author Tim Lambert
 */
@ServiceProvider(service = Spline.class)
public class Bezier implements Spline {

    private static final String name = "Bezier";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int minPoints() {
        return 3;
    }


    /**
     * The basis function for a Bezier spline
     *
     * @param i
     * @param t
     * @return
     */
    protected static double b(int i, double t) {
        switch (i) {
            case 0:
                return (1 - t) * (1 - t) * (1 - t);
            case 1:
                return 3 * t * (1 - t) * (1 - t);
            case 2:
                return 3 * t * t * (1 - t);
            case 3:
                return t * t * t;
        }
        return 0; //we only get here if an invalid i is specified
    }

    /**
     * Evaluate ith point on the B spline
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
        for (int j = 0; j <= 3; j++) {
            px += b(j, t) * xpoints[i + j];
            py += b(j, t) * ypoints[i + j];
        }
        return new Point2D.Double(px, py);
    }

    @Override
    public Point2D.Double[] curvePoints(double[] xpoints, double[] ypoints, int numPts, int steps) {
        int pts = numPts * steps / 3 + 1;
        Point2D.Double[] curve = new Point2D.Double[pts];
        curve[0] = point(0, 0, xpoints, ypoints);
        for (int i = 0; i < numPts - 3; i += 3) {
            for (int j = 1; j <= steps; j++) {
                curve[j] = point(i, j / (double) steps, xpoints, ypoints);
            }
        }
        return curve;
    }

}
