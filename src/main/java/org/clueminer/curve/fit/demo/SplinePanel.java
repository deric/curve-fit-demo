package org.clueminer.curve.fit.demo;

import java.awt.Color;
import java.awt.Event;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import org.clueminer.curve.fit.splines.Bezier;
import org.clueminer.curve.fit.splines.Spline;

/**
 * This panel allows users to experiment with splines.
 *
 * @author Tim Lambert
 */
public class SplinePanel extends JPanel {

    protected Spline curve = new Bezier();
    protected Color bgcolor = Color.WHITE;
    protected MouseEvent mouseEvent;
    private Thread canvasThread;
    private CurveCanvas canvas;

    public SplinePanel() {
        init();
    }

    private void init() {
        setLayout(new GridBagLayout());
        setBackground(bgcolor);
        mouseEvent = new MouseEvent();

        canvas = new CurveCanvas(mouseEvent, curve);

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        add(canvas, c);
        canvasThread = new Thread(canvas);
        canvasThread.start();
        canvasThread.setPriority(Thread.MIN_PRIORITY);
    }

    public void setSpline(Spline spline) {
        if (canvas != null) {
            this.curve = spline;
            canvas.setSpline(spline);
        }
    }

    @Override
    public boolean handleEvent(Event e) {
        if (e.id == Event.MOUSE_DOWN
                || e.id == Event.MOUSE_DRAG
                || e.id == Event.MOUSE_UP) {
            mouseEvent.put(e);
            return true;
        } else {
            return false;
        }
    }

    public void clear() {
        curve.reset();
        canvas.repaint();
    }
}
