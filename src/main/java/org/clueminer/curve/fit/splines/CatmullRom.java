package org.clueminer.curve.fit.splines;

import org.openide.util.lookup.ServiceProvider;

/**
 * @see http://www.cse.unsw.edu.au/~lambert/splines/
 * @author Tim Lambert
 */
@ServiceProvider(service = Spline.class)
public class CatmullRom extends BSpline {

    private static final String name = "CatmullRom";

    @Override
    public String getName() {
        return name;
    }

    /**
     * Catmull-Rom spline is just like a B spline, only with a different basis
     *
     * @param i
     * @param t
     * @return
     */
    @Override
    public float b(int i, float t) {
        switch (i) {
            case -2:
                return ((-t + 2) * t - 1) * t / 2;
            case -1:
                return (((3 * t - 5) * t) * t + 2) / 2;
            case 0:
                return ((-3 * t + 4) * t + 1) * t / 2;
            case 1:
                return ((t - 1) * t * t) / 2;
        }
        return 0; //we only get here if an invalid i is specified
    }

}
