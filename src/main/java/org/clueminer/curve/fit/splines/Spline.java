package org.clueminer.curve.fit.splines;

import java.awt.Graphics;

/**
 *
 * @author Tomas Barton
 */
public interface Spline {

    public String getName();

    /**
     * add a control point, return index of new control point
     *
     * @param x
     * @param y
     * @return index of added point
     */
    public int addPoint(int x, int y);

    public void reset();

    /**
     * return index of control point near to (x,y) or -1 if nothing near
     *
     * @param x
     * @param y
     * @return
     */
    public int selectPoint(int x, int y);

    /**
     * paint this curve into g.
     *
     * @param g
     */
    public void paint(Graphics g);

    public void setPoint(int x, int y);

    public void removePoint();

}
