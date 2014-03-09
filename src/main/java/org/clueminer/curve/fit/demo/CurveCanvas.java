package org.clueminer.curve.fit.demo;

import java.awt.Canvas;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;
import org.clueminer.curve.fit.splines.Spline;

/**
 * Canvas for painting controlled curves into
 */
public class CurveCanvas extends Canvas implements Runnable {

    private Image offscreen;
    private Spline curve;
    private MouseEvent mouseEvent;

    public CurveCanvas(MouseEvent mouseEvent, Spline curve) {
        this.mouseEvent = mouseEvent;
        this.curve = curve;
    }

    @Override
    public void paint(Graphics g) {
        if (offscreen == null) {
            offscreen = createImage(getSize().width, getSize().height);
        }
        Graphics og = offscreen.getGraphics();
        og.clearRect(0, 0, getSize().width, getSize().height);
        curve.paint(og);
        g.drawImage(offscreen, 0, 0, null);
    }

    public void setSpline(Spline spline) {
        curve = (ControlCurve) spline;
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    public void update() {
        update(getGraphics());
    }

    @Override
    public void run() {
        for (;;) {
            Event e = mouseEvent.get();
            if (e.id == Event.MOUSE_DOWN) {
                if (curve.selectPoint(e.x, e.y) == -1) { /*no point selected add new one*/

                    curve.addPoint(e.x, e.y);
                    update();
                }
            } else if (e.id == Event.MOUSE_DRAG) {
                curve.setPoint(e.x, e.y);
                update();
            } else if (e.id == Event.MOUSE_UP) {
                if (e.shiftDown()) {
                    curve.removePoint(); //Shift Click removes control points
                    update();
                }
            }
        }
    }

}
