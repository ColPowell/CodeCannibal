package org.dreambot.decomplier.gui.component.impl;

import org.dreambot.decomplier.gui.DecompilerFrame;
import org.dreambot.decomplier.gui.component.DComponent;
import org.dreambot.decomplier.gui.component.impl.center.DCenterPane;
import org.dreambot.decomplier.gui.component.impl.top.DTopToolBar;
import org.dreambot.decomplier.gui.component.impl.west.DWestPane;

import javax.swing.*;
import java.awt.*;

/**
 * @author Notorious BPP
 * @since 7/16/2016.
 */
public class DContentPane extends JPanel implements DComponent<DecompilerFrame> {

    private final DecompilerFrame owner;
    private final DWestPane<DContentPane> westPane;
    private final DTopToolBar<DContentPane> toolBar;
    private final DCenterPane<DContentPane> centerPane;

    public DContentPane(DecompilerFrame owner) {
        super(new BorderLayout());
        this.owner = owner;
        westPane = new DWestPane<>(this);
        toolBar = new DTopToolBar<>(this);
        centerPane = new DCenterPane<>(this);

        add(toolBar, BorderLayout.NORTH);
        add(centerPane, BorderLayout.CENTER);
        add(westPane, BorderLayout.WEST);
    }

    public DWestPane<DContentPane> getWestPane() {
        return westPane;
    }

    public DCenterPane<DContentPane> getCenterPane() {
        return centerPane;
    }

    @Override
    public DecompilerFrame getOwner() {
        return owner;
    }
}
