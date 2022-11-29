package quotes.gui.quote;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import quotes.database.Person;

/**
 * Renders a {@code Person} object in a {@code JComboBox}
 */
public class PersonSelectorRenderer extends JLabel implements ListCellRenderer<Person> {
    public PersonSelectorRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Person> list, Person p, int index,
            boolean isSelected, boolean cellHasFocus) {
        if (p != null) {
            String text = p.getFullName();
            if (!p.getNotes().isEmpty())
                text += " (" + p.getNotes() + ")";
            setText(text);
        }

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        return this;
    }

}
