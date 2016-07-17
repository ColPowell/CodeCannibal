package org.dreambot.decomplier.gui.component.impl.west;

import org.dreambot.decomplier.gui.component.DComponent;
import org.dreambot.decomplier.gui.component.impl.west.impl.DFilePane;
import org.dreambot.decomplier.gui.component.impl.west.impl.DSearchPane;
import org.dreambot.decomplier.gui.component.util.ComponentResizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * @author Notorious BPP
 * @since 7/16/2016.
 */
public class DWestPane<C extends Component> extends JPanel implements DComponent<C> {

    private final DFilePane<DWestPane<C>> filePane;
    private final DSearchPane<DWestPane<C>> searchPane;
    private C owner;

    public DWestPane(C owner) {
        super(new GridLayout(2, 1));
        this.owner = owner;

        filePane = new DFilePane<>(this);
        searchPane = new DSearchPane<>(this);

        init(owner);
        makeResizable();

        add(filePane);
        add(searchPane);
    }

    private void init(C owner) {
        Dimension dimension = new Dimension(256, owner.getHeight());
        setPreferredSize(dimension);
        setMinimumSize(dimension);
    }

    private void makeResizable() {
        ComponentResizer.makeResizable(filePane);
        final DWestPane instance = this;
        filePane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                filePane.setLocation(0, 0);
                searchPane.setLocation(0, filePane.getHeight());
                searchPane.setSize(getWidth(), instance.getHeight() - filePane.getHeight());
            }
        });
    }

    public DFilePane<DWestPane<C>> getFilePane() {
        return filePane;
    }

    @Override
    public C getOwner() {
        return owner;
    }
}
