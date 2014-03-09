package org.clueminer.curve.fit.splines;

import org.openide.util.lookup.ServiceProvider;

/**
 * Bezier spline with G1 continuity between control points
 *
 * @see http://www.cse.unsw.edu.au/~lambert/splines/
 * @author Tim Lambert
 */
@ServiceProvider(service = Spline.class)
public class BezierG1 extends Bezier {

    private static final String name = "Bezier G1";

    @Override
    public String getName() {
        return name;
    }

    /**
     * Ensure G1 continuity by forcing control points to be collinear.
     * If 0 1 2 3 4 5 6 are the the control points, then for the
     * Beziers 0123 and 3456 to be G1 continuous at their join, 2, 3
     * and 4 must be collinear
     */
    int deltax;
    int deltay;

    /* adjust positions of points so that points are collinear */
    void forceCollinear(int i) {
        if (i % 3 == 0 && i < pts.npoints - 1 && i > 0) { //interpolating control point
            pts.xpoints[i - 1] += deltax;  //adjust neighbours
            pts.ypoints[i - 1] += deltay;  // by the same amount
            pts.xpoints[i + 1] += deltax;  // that this one has changed
            pts.ypoints[i + 1] += deltay;
        } else if (i % 3 == 1 && i > 1) {
            forceCollinear(i, i - 1, i - 2);
        } else if (i % 3 == 2 && i < pts.npoints - 2) {
            forceCollinear(i, i + 1, i + 2);
        }
    }

    /* move k such that it is collinear with i and j */
    void forceCollinear(int i, int j, int k) {
        float ij = distance(pts.xpoints[i], pts.ypoints[i], pts.xpoints[j], pts.ypoints[j]);
        float jk = distance(pts.xpoints[j], pts.ypoints[j], pts.xpoints[k], pts.ypoints[k]);
        float r = jk / ij;
        pts.xpoints[k] = Math.round(pts.xpoints[j] + r * (pts.xpoints[j] - pts.xpoints[i]));
        pts.ypoints[k] = Math.round(pts.ypoints[j] + r * (pts.ypoints[j] - pts.ypoints[i]));
    }

    float distance(int x1, int y1, int x2, int y2) {
        return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    /**
     * add a control point, return index of new control point
     */
    public int addPoint(int x, int y) {
        int i = super.addPoint(x, y);
        forceCollinear(i);
        return i;
    }

    /**
     * set selected control point
     */
    public void setPoint(int x, int y) {
        deltax = x - pts.xpoints[selection]; //save previous value
        deltay = y - pts.ypoints[selection]; //save previous value
        super.setPoint(x, y);
        forceCollinear(selection);
    }

    /**
     * remove selected control point
     */
    public void removePoint() {
        super.removePoint();
        for (int i = 4; i < pts.npoints; i += 3) {
            forceCollinear(i);
        }
    }

}
