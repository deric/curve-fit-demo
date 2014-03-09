package org.clueminer.curve.fit.demo;

import java.awt.Canvas;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;
import java.util.LinkedList;
import java.util.List;

/**
 * Canvas for painting curves
 */
public class CurveCanvas extends Canvas implements Runnable {

    private Image offscreen;
    private MouseEvent mouseEvent;
    private transient List<CanvasListener> listeners;

    public CurveCanvas(MouseEvent mouseEvent) {
        this.listeners = new LinkedList<>();
        this.mouseEvent = mouseEvent;
    }

    @Override
    public void paint(Graphics g) {
        if (offscreen == null) {
            offscreen = createImage(getSize().width, getSize().height);
        }
        Graphics og = offscreen.getGraphics();
        og.clearRect(0, 0, getSize().width, getSize().height);
        firePaint(og);
        g.drawImage(offscreen, 0, 0, null);
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
                if (fireSelectPoint(e.x, e.y) == -1) { /*no point selected add new one*/

                    fireAddPoint(e.x, e.y);
                    update();
                }
            } else if (e.id == Event.MOUSE_DRAG) {
                fireSetPoint(e.x, e.y);
                update();
            } else if (e.id == Event.MOUSE_UP) {
                if (e.shiftDown()) {
                    fireRemovePoint(); //Shift Click removes control points
                    update();
                }
            }
        }
    }

    public void addListener(CanvasListener l) {
        listeners.add(l);
    }

    public void removeLogListener(CanvasListener l) {
        listeners.remove(l);
    }

    private void firePaint(Graphics og) {
        for (CanvasListener l : listeners) {
            l.paint(og);
        }
    }

    private void fireRemovePoint() {
        for (CanvasListener l : listeners) {
            l.removePoint();
        }
    }

    private void fireSetPoint(int x, int y) {
        for (CanvasListener l : listeners) {
            l.setPoint(x, y);
        }
    }

    private void fireAddPoint(int x, int y) {
        for (CanvasListener l : listeners) {
            l.addPoint(x, y);
        }
    }

    private int fireSelectPoint(int x, int y) {
        int ret = -1;
        for (CanvasListener l : listeners) {
            ret = l.selectPoint(x, y);
            if (ret > -1) {
                return ret;
            }
        }
        return ret;
    }

}
