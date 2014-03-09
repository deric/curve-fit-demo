package org.clueminer.curve.fit.demo;

import org.clueminer.curve.fit.splines.Spline;

/**
 *
 * @author Tomas Barton
 */
public class Controller {

    private SplinePanel panel;

    public Controller(SplinePanel panel) {
        this.panel = panel;
    }

    /**
     * Removes all elements form the canvas
     */
    public void clear() {
        panel.clear();
    }

    public void setCurve(Spline curve) {
        panel.setSpline(curve);
    }

}
