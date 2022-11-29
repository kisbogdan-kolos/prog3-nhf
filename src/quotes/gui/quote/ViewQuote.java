package quotes.gui.quote;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import quotes.database.Quote;

public class ViewQuote extends JFrame {
    private JTextArea textArea;

    /**
     * Creates a {@code ViewQuote} object
     */
    public ViewQuote() {
        super("Idézet nézet");
        setSize(new Dimension(400, 200));

        textArea = new JTextArea();
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setBackground(UIManager.getColor("Label.background"));
        textArea.setFont(UIManager.getFont("Label.font"));
        textArea.setBorder(UIManager.getBorder("Label.border"));
        textArea.setFont(textArea.getFont().deriveFont(20.0f));
        textArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        add(textArea, BorderLayout.CENTER);

        JButton bt = new JButton("Bezár");
        bt.addActionListener(e -> setVisible(false));
        JPanel p = new JPanel();
        p.add(bt);

        add(p, BorderLayout.SOUTH);
    }

    /**
     * @return the text area with the quote
     */
    public JTextArea getTextArea() {
        return textArea;
    }

    /**
     * Shows the component
     */
    public void showComponent(Quote q) {
        setQuote(q);
        setVisible(true);
    }

    /**
     * Sets the quote to display
     * 
     * @param q the quote to display
     */
    public void setQuote(Quote q) {
        String quoteText = "";
        if (!q.getContextBefore().isEmpty()) {
            quoteText += "[" + q.getContextBefore() + "] ";
        }
        quoteText += q.getQuote();
        if (!q.getContextAfter().isEmpty()) {
            quoteText += " [" + q.getContextAfter() + "]";
        }

        quoteText += " - " + q.getAuthor().getShortName();

        textArea.setText(quoteText);
    }

}
