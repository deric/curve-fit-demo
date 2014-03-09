package org.clueminer.curve.fit.demo;

import java.awt.Graphics;

/**
 *
 * @author Tomas Barton
 */
public interface CanvasListener {

    /**
     * Paint given graphics
     *
     * @param g
     */
    public void paint(Graphics g);

    public int selectPoint(int x, int y);

    public void addPoint(int x, int y);

    public void setPoint(int x, int y);

    public void removePoint();

}
