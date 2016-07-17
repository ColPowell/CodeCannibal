package org.dreambot.decomplier.gui.component.impl.west.impl;

import org.dreambot.decomplier.gui.component.DComponent;

import javax.swing.*;
import java.awt.*;

/**
 * @author Notorious BPP
 * @since 7/16/2016.
 */
public class DSearchPane<C extends Component> extends JPanel implements DComponent<C> {

    private final C owner;

    public DSearchPane(C owner) {
        this.owner = owner;
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(-1, 0, 0, 0), BorderFactory.createLineBorder(getBackground().darker())));
    }

    @Override
    public C getOwner() {
        return owner;
    }
}
