package org.clueminer.curve.fit.splines;

import java.awt.geom.Point2D;

/**
 *
 * @author Tomas Barton
 */
public interface Spline {

    public String getName();

    /**
     * Compute position of ith point from arrays of X and Y coordinates
     *
     * @param i
     * @param t
     * @param xpoints
     * @param ypoints
     * @return
     */
    public Point2D.Double point(int i, double t, double[] xpoints, double[] ypoints);

    /**
     *
     * @param xpoints
     * @param ypoints
     * @param steps
     * @return
     */
    public Point2D.Double[] curvePoints(double[] xpoints, double[] ypoints, int steps);

}
