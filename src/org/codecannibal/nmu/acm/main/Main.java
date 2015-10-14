package org.codecannibal.nmu.acm.main;

import org.codecannibal.nmu.acm.main.gui.CodeCannibleGUI;

import java.util.Collections;
import java.util.List;

/**
 * Created by Colton on 10/1/2015.
 */
public class Main {

    //make a GUI for the program
    public static void main(String[] args){
        //String testFile = "C:\\Users\\Colton\\IdeaProjects\\CodeCannibal\\out\\artifacts\\testJar\\testJar.jar";
        String testFile = "C:\\Users\\Colton\\IdeaProjects\\Deobber\\gamepacks\\gamepack94.jar";
        JarOpener jarOpener = new JarOpener(testFile);
        CodeCannibleGUI gui = new CodeCannibleGUI(jarOpener);
        gui.setVisible(true);

        List<String> list = jarOpener.getAllClassNames();
        Collections.sort(list);
        gui.updateClassTree(list);
    }
}
