package org.clueminer.curve.fit.demo;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.LinkedHashMap;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import org.clueminer.curve.fit.splines.Spline;
import org.openide.util.Lookup;

/**
 *
 * @author Tomas Barton
 */
public class SplineToolbar extends JPanel implements ActionListener {

    private JToolBar toolbar;
    private JComboBox comboSpline;
    private JButton btnClear;
    private final Controller control;
    protected LinkedHashMap<String, Spline> providers;

    public SplineToolbar(Controller control) {
        this.control = control;
        init();
    }

    private void init() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        toolbar = new JToolBar(SwingConstants.HORIZONTAL);
        JLabel label = new JLabel("Spline/Interpolator: ");
        toolbar.add(label);
        comboSpline = new JComboBox(getProviders());
        comboSpline.addActionListener(this);
        toolbar.add(comboSpline);
        btnClear = new JButton("Clear");
        toolbar.add(btnClear);
        btnClear.addActionListener(this);
        toolbar.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(toolbar);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(btnClear)) {
            control.clear();
        } else if (source.equals(comboSpline)) {
            String item = (String) comboSpline.getSelectedItem();
            control.setCurve(providers.get(item));
            System.out.println("selected:" + item);
        }
    }

    public String[] getProviders() {
        Collection<? extends Spline> list = Lookup.getDefault().lookupAll(Spline.class);
        String[] res = new String[list.size()];
        providers = new LinkedHashMap<>();
        int i = 0;
        for (Spline spl : list) {
            providers.put(spl.getName(), spl);
            res[i++] = spl.getName();
        }
        return res;
    }

}
