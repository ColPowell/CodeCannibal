package org.dreambot.decomplier.gui.component.impl.center;

import org.dreambot.decomplier.gui.component.DComponent;
import org.dreambot.decomplier.gui.component.impl.center.impl.DSearchToolBar;
import org.dreambot.decomplier.gui.component.impl.center.impl.DTextPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author Notorious BPP
 * @since 7/16/2016.
 */
public class DCenterPane<C extends Component> extends JPanel implements DComponent<C> {

    private final DTextPane<DCenterPane<C>> textPane;
    private final DSearchToolBar<DCenterPane<C>> toolBar;
    private C owner;

    public DCenterPane(C owner) {
        this.owner = owner;
        textPane = new DTextPane<>(this);
        toolBar = new DSearchToolBar<>(this);
        textPane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_F){
                    toolBar.setVisible(!toolBar.isVisible());
                }
            }
        });

        init(owner);
        toolBar.setVisible(false);
        add(toolBar, BorderLayout.NORTH);
        add(new JScrollPane(textPane), BorderLayout.CENTER);
    }

    private void init(C owner) {
        Dimension dimension = new Dimension(owner.getWidth() - 256, owner.getHeight() - 75);
        setLayout(new BorderLayout());
        setPreferredSize(dimension);
        setMinimumSize(dimension);
        setBorder(BorderFactory.createLineBorder(getBackground().darker()));
    }

    public DTextPane<DCenterPane<C>> getTextPane() {
        return textPane;
    }

    @Override
    public C getOwner() {
        return owner;
    }
}
