package org.clueminer.curve.fit.demo;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.openide.util.Exceptions;

public class CurveFitDemo extends JFrame {

    private CurvePanel panel;
    private SplineToolbar toolbar;
    private Controller control;

    public CurveFitDemo() {
        init();
    }

    private static void createAndShowGUI() throws Exception {
        CurveFitDemo frame = new CurveFitDemo();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    createAndShowGUI();
                } catch (Exception e) {
                    Exceptions.printStackTrace(e);
                }
            }
        });
    }

    private void init() {
        setLayout(new GridBagLayout());
        panel = new CurvePanel();
        control = new Controller(panel);

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1.0;
        c.weighty = 0.1;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;

        toolbar = new SplineToolbar(control);
        add(toolbar, c);
        c.gridy = 1;
        c.weighty = 0.9;
        add(panel, c);

        setVisible(true);
    }
}
