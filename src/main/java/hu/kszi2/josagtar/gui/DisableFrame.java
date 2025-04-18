package hu.kszi2.josagtar.gui;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;

/**
 * Attached to a {@code JFrame} object, when the frame is visible, the
 * {@code JFrame} given in the constructor is disabled
 * and cannot be used until the frame is closed
 */
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
