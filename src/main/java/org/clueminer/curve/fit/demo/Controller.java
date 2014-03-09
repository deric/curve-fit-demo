package org.clueminer.curve.fit.demo;

import java.awt.Graphics;
import org.clueminer.curve.fit.splines.Bezier;
import org.clueminer.curve.fit.splines.Spline;

/**
 *
 * @author Tomas Barton
 */
public class Controller implements CanvasListener {

    private final SplinePanel panel;
    protected Spline model = new Bezier();

    public Controller(SplinePanel panel) {
        this.panel = panel;
        init();
    }

    private void init() {
        panel.addListener(this);
    }

    /**
     * Removes all elements form the canvas
     */
    public void clear() {
        panel.clear();
        model.reset();
    }

    public void setSpline(Spline spline) {
        this.model = spline;
    }

    @Override
    public void paint(Graphics g) {
        model.paint(g);
    }

    @Override
    public int selectPoint(int x, int y) {
        return model.selectPoint(x, y);
    }

    @Override
    public void addPoint(int x, int y) {
        model.addPoint(x, y);
    }

    @Override
    public void setPoint(int x, int y) {
        model.setPoint(x, y);
    }

    @Override
    public void removePoint() {
        model.removePoint();
    }

}
