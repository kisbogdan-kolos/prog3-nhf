package quotes.gui;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;

/**
 * Render a {@code JButton} object in a {@code JTable}
 */
public class JButtonTableCellEditor extends DefaultCellEditor {
    public JButtonTableCellEditor(JCheckBox checkBox) {
        super(checkBox);
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        return (JButton) value;
    }
}
