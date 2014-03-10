package org.clueminer.curve.fit.demo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;

/**
 * Canvas for painting curves
 */
public class CurvePanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener, ComponentListener {

    private Image offscreen;
    private final transient List<CanvasListener> listeners;
    private static boolean isShiftDown = false;

    public CurvePanel() {
        this.listeners = new LinkedList<>();
        init();
    }

    private void init() {
        this.addMouseListener(this);
        this.addKeyListener(this);
        this.addComponentListener(this);
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

    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {
        if (fireSelectPoint(e.getX(), e.getY()) == -1) { /*no point selected add new one*/

            fireAddPoint(e.getX(), e.getY());
            update();
        }
        System.out.println("mouse clicked: " + e.getX() + ", " + e.getY());

        //drawCircle(e.getX(), e.getY());
        repaint();
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {

    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        if (isShiftDown) {
            fireRemovePoint(); //Shift Click removes control points
            update();
        }
    }

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {
        //
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {
        //
    }

    @Override
    public void mouseDragged(java.awt.event.MouseEvent e) {
        fireSetPoint(e.getX(), e.getY());
        update();
    }

    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) {
        //
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.SHIFT_DOWN_MASK) {
            isShiftDown = true;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.SHIFT_MASK) {
            isShiftDown = false;
        }
    }

    public void drawCircle(int x, int y) {
        Graphics g = this.getGraphics();
        g.drawOval(x, y, x, y);
        g.setColor(Color.BLACK);
        g.fillOval(x, y, 2, 2);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        offscreen = null; //invalidate cache
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        //
    }

    @Override
    public void componentShown(ComponentEvent e) {
        //
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        //
    }
}
