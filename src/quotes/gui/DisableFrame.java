package quotes.gui;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;

public class DisableFrame implements ComponentListener {
    private JFrame frame;

    public DisableFrame(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        frame.setEnabled(true);
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentResized(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
        frame.setEnabled(false);
    }

}
