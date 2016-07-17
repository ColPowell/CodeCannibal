package org.dreambot.decomplier;

import org.codecannibal.nmu.acm.main.JarOpener;
import org.dreambot.decomplier.gui.DecompilerFrame;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Notorious BPP
 * @since 7/16/2016.
 */
public class Decompiler {

    private static JarOpener opener;
    public static DecompilerFrame frame;

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        frame = new DecompilerFrame();
        SwingUtilities.invokeAndWait(() -> {
            frame.setVisible(true);
        });
    }

    public static JarOpener getOpener() {
        return opener;
    }

    public static void setOpener(JarOpener opener) {
        Decompiler.opener = opener;
    }

    public static DecompilerFrame getFrame() {
        return frame;
    }

    public static void exit() {
        System.exit(0); //add exit crap here
    }
}
