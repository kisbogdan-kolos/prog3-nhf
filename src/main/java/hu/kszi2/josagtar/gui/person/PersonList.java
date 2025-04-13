package hu.kszi2.josagtar.gui.person;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import hu.kszi2.josagtar.PersonData;
import hu.kszi2.josagtar.gui.DisableFrame;
import hu.kszi2.josagtar.gui.JButtonTableCellEditor;
import hu.kszi2.josagtar.gui.JButtonTableCellRenderer;

public class PersonList extends JFrame {

    private PersonData pd;
    private AddPerson ap;
    private EditPerson ep;

    /**
     * Creates a {@code PersonList} object
     * 
     * @param pd the {@code PersonData} object to use
     */
    public PersonList(PersonData pd) {
        super("Emberek listája");
        this.pd = pd;

        ap = new AddPerson(pd);
        ep = new EditPerson(pd);

        pd.addEditEvent(p -> ep.showComponent(p));

        ap.addComponentListener(new DisableFrame(this));
        ep.addComponentListener(new DisableFrame(this));

        initComponents();
    }

    /**
     * Shows the component
     */
    public void showComponent() {
        setVisible(true);
    }

    /**
     * Initializes the components
     */
    private void initComponents() {
        setMinimumSize(new Dimension(600, 300));
        setSize(new Dimension(1200, 600));

        setLocationRelativeTo(null);

        JTable table = new JTable();
        table.setFillsViewportHeight(true);
        table.setModel(pd);
        table.setDefaultRenderer(JButton.class, new JButtonTableCellRenderer());
        table.setDefaultEditor(JButton.class, new JButtonTableCellEditor(new JCheckBox()));
        JScrollPane panel = new JScrollPane(table);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        add(panel, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton addNew = new JButton("Új ember hozzáadása");
        addNew.addActionListener(e -> {
            ap.showComponent();
        });
        bottom.add(addNew);
        bottom.setBorder(new EmptyBorder(0, 10, 10, 10));

        add(bottom, BorderLayout.SOUTH);
    }
}
