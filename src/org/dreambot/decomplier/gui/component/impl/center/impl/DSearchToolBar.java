package org.dreambot.decomplier.gui.component.impl.center.impl;

import org.dreambot.decomplier.gui.component.DComponent;

import javax.swing.*;
import java.awt.*;

/**
 * @author Notorious BPP
 * @since 7/16/2016.
 */
public class DSearchToolBar<C extends Component> extends JToolBar implements DComponent<C> {

    private C owner;

    public DSearchToolBar(C owner) {
        this.owner = owner;

        init(owner);
    }

    private void init(C owner) {
        Dimension dimension = new Dimension(owner.getWidth(), 25);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
    }

    @Override
    public C getOwner() {
        return owner;
    }
}
