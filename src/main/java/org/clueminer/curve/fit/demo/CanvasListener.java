package org.clueminer.curve.fit.demo;

import java.awt.Graphics2D;

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
    public void paint(Graphics2D g);

    /**
     * Find point near or at given coordinates
     *
     * @param x
     * @param y
     * @return -1 if no point was found
     */
    public int findPoint(int x, int y);

    /**
     * Add point and return its index
     *
     * @param x
     * @param y
     * @return index new point
     */
    public int addPoint(int x, int y);

    public void setPoint(int x, int y);

    public void removePoint();

}
