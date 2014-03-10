package org.clueminer.curve.fit.splines;

import java.awt.geom.Point2D;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @see http://www.cse.unsw.edu.au/~lambert/splines/
 * @author Tim Lambert
 */
@ServiceProvider(service = Spline.class)
public class NatCubic implements Spline {

    private static final String name = "NatCubic";

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int minPoints() {
        return 2;
    }

    /**
     * calculates the natural cubic spline that interpolates y[0], y[1], ...
     * y[n] The first segment is returned as C[0].a + C[0].b*u + C[0].c*u^2 +
     * C[0].d*u^3 0<=u <1 the other segments are in C[1], C[2], ... C[n-1]
     * @param n @param x @return
     */
    public Cubic[] calcNaturalCubic(int n, double[] x) {
        double[] gamma = new double[n + 1];
        double[] delta = new double[n + 1];
        double[] D = new double[n + 1];
        int i;
        /* We solve the equation
         [2 1       ] [D[0]]   [3(x[1] - x[0])  ]
         |1 4 1     | |D[1]|   |3(x[2] - x[0])  |
         |  1 4 1   | | .  | = |      .         |
         |    ..... | | .  |   |      .         |
         |     1 4 1| | .  |   |3(x[n] - x[n-2])|
         [       1 2] [D[n]]   [3(x[n] - x[n-1])]

         by using row operations to convert the matrix to upper triangular
         and then back sustitution.  The D[i] are the derivatives at the knots.
         */

        gamma[0] = 1.0f / 2.0f;
        for (i = 1; i < n; i++) {
            gamma[i] = 1 / (4 - gamma[i - 1]);
        }
        gamma[n] = 1 / (2 - gamma[n - 1]);

        delta[0] = 3 * (x[1] - x[0]) * gamma[0];
        for (i = 1; i < n; i++) {
            delta[i] = (3 * (x[i + 1] - x[i - 1]) - delta[i - 1]) * gamma[i];
        }
        delta[n] = (3 * (x[n] - x[n - 1]) - delta[n - 1]) * gamma[n];

        D[n] = delta[n];
        for (i = n - 1; i >= 0; i--) {
            D[i] = delta[i] - gamma[i] * D[i + 1];
        }

        /* now compute the coefficients of the cubics */
        Cubic[] C = new Cubic[n];
        for (i = 0; i < n; i++) {
            C[i] = new Cubic((float) x[i], D[i], 3 * (x[i + 1] - x[i]) - 2 * D[i] - D[i + 1],
                    2 * (x[i] - x[i + 1]) + D[i] + D[i + 1]);
        }
        return C;
    }

    /**
     *
     * @return
     */
    /*  public List<Point> generatePoints() {        List<Point> points = new ArrayList<>();

     Point next;
     if (pts.npoints >= 2) {
     Cubic[] xPolyn = calcNaturalCubic(pts.npoints - 1, pts.xpoints);
     Cubic[] yPolyn = calcNaturalCubic(pts.npoints - 1, pts.ypoints);
     next = new Point((int) Math.round(xPolyn[0].eval(0)), (int) Math.round(yPolyn[0].eval(0)));
     points.add(next);

     for (int i = 0; i < xPolyn.length; i++) {   //for each cubic calculated

     int stepxForSegment = Math.abs(pts.xpoints[i + 1] - pts.xpoints[i]);   //evaluated the max distance to calculate the point to draw for each interval
     int stepyForSegment = Math.abs(pts.ypoints[i + 1] - pts.ypoints[i]);   //this is done to maintain constant the number of point used to draw the line
     int stepForSegment = Math.max(stepxForSegment, stepyForSegment);

     //int stepForSegment = 12                                 //uncoment this row if u want to see the difference and comment the upper row
     for (int j = 1; j <= stepForSegment; j++) {
     float u = j / (float) stepForSegment;
     next = new Point(Math.round(xPolyn[i].eval(u)), Math.round(yPolyn[i].eval(u)));
     points.add(next);
     }
     }
     }
     return points;
     }
     */

    @Override
    public Point2D.Double[] curvePoints(double[] xpoints, double[] ypoints, int numPts, int steps) {
        Point2D.Double[] curve;
        if (numPts >= 2) {
            Cubic[] X = calcNaturalCubic(numPts - 1, xpoints);
            Cubic[] Y = calcNaturalCubic(numPts - 1, ypoints);

            curve = new Point2D.Double[X.length * steps + 1];
            curve[0] = new Point2D.Double(X[0].eval(0), Y[0].eval(0));
            int k = 1;
            for (int i = 0; i < X.length; i++) {
                for (int j = 1; j <= steps; j++) {
                    double u = j / (double) steps;
                    curve[k++] = new Point2D.Double(X[i].eval(u), Y[i].eval(u));
                }
            }
        } else {
            return new Point2D.Double[0];
        }
        return curve;
    }

    @Override
    public Point2D.Double point(int i, double t, double[] xpoints, double[] ypoints) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
