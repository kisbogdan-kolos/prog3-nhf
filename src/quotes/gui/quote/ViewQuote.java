package quotes.gui.quote;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import quotes.database.Quote;

public class ViewQuote extends JFrame {
    private JTextArea textArea;

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

        add(textArea, BorderLayout.CENTER);

        JButton bt = new JButton("Bezár");
        bt.addActionListener(e -> setVisible(false));
        JPanel p = new JPanel();
        p.add(bt);

        add(p, BorderLayout.SOUTH);
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public void showComponent(Quote q) {
        setQuote(q);
        setVisible(true);
    }

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