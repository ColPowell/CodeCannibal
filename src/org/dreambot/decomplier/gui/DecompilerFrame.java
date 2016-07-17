package org.dreambot.decomplier.gui;

import org.dreambot.decomplier.gui.component.impl.DContentPane;

import javax.swing.*;

/**
 * @author Notorious BPP
 * @since 7/16/2016.
 */
public class DecompilerFrame extends JFrame {

    private final DContentPane contentPane;

    public DecompilerFrame() {
        super("Ice Cream Maker Deluxe 7000 v3 .2 Extreme");
        contentPane = new DContentPane(this);

        setSize(800, 600);
        setLocationRelativeTo(null);
        setContentPane(contentPane);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    @Override
    public DContentPane getContentPane() {
        return contentPane;
    }
}
