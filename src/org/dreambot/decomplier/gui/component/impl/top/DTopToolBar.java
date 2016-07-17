package org.dreambot.decomplier.gui.component.impl.top;

import org.codecannibal.nmu.acm.main.JarOpener;
import org.dreambot.decomplier.Decompiler;
import org.dreambot.decomplier.gui.component.DComponent;
import org.dreambot.decomplier.gui.component.util.JarTreeModel;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * @author Notorious BPP
 * @since 7/16/2016.
 */
public class DTopToolBar<C extends Component> extends JMenuBar implements DComponent<C> {

    private C owner;

    public DTopToolBar(C owner) {
        this.owner = owner;

        init(owner);
        add(getFileMenu());
        add(getEditMenu());
        add(getViewMenu());
        add(getSettingsMenu());
    }

    private void init(C owner) {
        Dimension dimension = new Dimension(owner.getWidth(), 25);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
    }

    private JMenu getFileMenu() {
        JMenu menu = new JMenu("File");
        final JFileChooser fc = new JFileChooser(".jar");
        JMenuItem open = new JMenuItem(new AbstractAction("Open") {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnVal = fc.showOpenDialog(DTopToolBar.this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    if(file.exists()){
                        Decompiler.setOpener(new JarOpener(file.getPath()));
                        Decompiler.getFrame().getContentPane().getWestPane().getFilePane().setModel(new JarTreeModel(file));
                    }
                }
            }
        });
        JMenuItem close = new JMenuItem(new AbstractAction("Close") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Decompiler.getFrame().getContentPane().getCenterPane().getTextPane().setDocument(new DefaultStyledDocument());
            }
        });
        JMenuItem exit = new JMenuItem(new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                Decompiler.exit();
            }
        });
        menu.add(open);
        menu.add(close);
        menu.add(exit);

        return menu;
    }

    private JMenu getEditMenu() {
        JMenu menu = new JMenu("Edit");
        return menu;
    }

    private JMenu getViewMenu() {
        JMenu menu = new JMenu("View");
        return menu;
    }

    private JMenu getSettingsMenu() {
        JMenu menu = new JMenu("Settings");
        return menu;
    }


    @Override
    public C getOwner() {
        return owner;
    }
}
