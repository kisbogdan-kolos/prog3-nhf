package hu.kszi2.josagtar.gui.quote;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import hu.kszi2.josagtar.QuoteData;
import hu.kszi2.josagtar.gui.DisableFrame;
import hu.kszi2.josagtar.gui.JButtonTableCellEditor;
import hu.kszi2.josagtar.gui.JButtonTableCellRenderer;

public class QuoteList extends JFrame {
    private QuoteData qd;
    private AddQuote aq;
    private EditQuote eq;
    private ViewQuote vq;
    private QuoteFilter qf;

    /**
     * Creates a {@code QuoteList} object
     * 
     * @param qd the {@code QuoteData} object to use
     */
    public QuoteList(QuoteData qd) {
        super("Idézetek listája");
        this.qd = qd;

        aq = new AddQuote(qd);
        eq = new EditQuote(qd);
        vq = new ViewQuote();

        qd.addEditEvent(q -> eq.showComponent(q));
        qd.addViewEvent(q -> vq.showComponent(q));

        aq.addComponentListener(new DisableFrame(this));
        eq.addComponentListener(new DisableFrame(this));
        vq.addComponentListener(new DisableFrame(this));

        initComponents();
    }

    /**
     * Shows the component
     */
    public void showComponent() {
        qf.updateList();

        setVisible(true);
    }

    /**
     * Shows the component
     * 
     * @param autoAdd if the {@code AddQuote} component should be shown
     */
    public void showComponent(boolean autoAdd) {
        showComponent();

        if (autoAdd)
            aq.showComponent();
    }

    /**
     * Initializes the components
     */
    private void initComponents() {
        setMinimumSize(new Dimension(600, 300));
        setSize(new Dimension(1200, 600));

        setLocationRelativeTo(null);

        JTable table = new JTable();
        qf = new QuoteFilter(qd, table);
        add(qf, BorderLayout.NORTH);

        table.setFillsViewportHeight(true);
        table.setModel(qd);
        table.setDefaultRenderer(JButton.class, new JButtonTableCellRenderer());
        table.setDefaultEditor(JButton.class, new JButtonTableCellEditor(new JCheckBox()));
        JScrollPane panel = new JScrollPane(table);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        add(panel, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton addNew = new JButton("Új idézet hozzáadása");
        addNew.addActionListener(e -> {
            aq.showComponent();
        });
        bottom.add(addNew);
        bottom.setBorder(new EmptyBorder(0, 10, 10, 10));

        add(bottom, BorderLayout.SOUTH);
    }
}
