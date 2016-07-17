package org.dreambot.decomplier.gui.component.impl.west.impl;

import org.codecannibal.nmu.acm.asm.block.impl.ClassBlock;
import org.codecannibal.nmu.acm.asm.block.impl.MethodBlock;
import org.dreambot.decomplier.Decompiler;
import org.dreambot.decomplier.gui.component.DComponent;
import org.dreambot.decomplier.gui.component.util.JarTreeModel;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Notorious BPP
 * @since 7/16/2016.
 */
public class DFilePane<C extends Component> extends JPanel implements DComponent<C> {

    private final C owner;
    private final JTree fileTree;
    private JarTreeModel model;
    private TraceMethodVisitor mp;
    private Printer printer;

    public DFilePane(C owner) {
        this.owner = owner;
        model = new JarTreeModel(null);
        this.printer = new Textifier();
        this.mp = new TraceMethodVisitor(printer);
        setLayout(new GridLayout(1, 1));
        setBorder(BorderFactory.createLineBorder(getBackground().darker()));
        fileTree = new JTree(model);
        add(new JScrollPane(fileTree));
        fileTree.addTreeSelectionListener(e -> {
            String s = "";
            for(int i = 1; i < e.getPath().getPath().length; i++){
                s += (s.isEmpty() ? "" : "/") + e.getPath().getPath()[i];
            }
            ClassBlock classBlock = Decompiler.getOpener().getClassBlocks().get(s.replace(".class", ""));
            if(classBlock != null){
                String scan = scanClass(classBlock);
                Decompiler.getFrame().getContentPane().getCenterPane().getTextPane().setText(scan);
            }
        });
    }

    private String scanClass(ClassBlock classNode) {
        String byteCode = "";
        for(MethodBlock o : classNode.getMethods()){
            MethodNode methodNode = o.getNode();
            byteCode += scanMethod(methodNode);
        }

        return byteCode;
    }

    private String scanMethod(MethodNode methodNode){
        ASMifier asMifier = new ASMifier();
        asMifier.getText();

        StringBuilder sb = new StringBuilder();
        for(AbstractInsnNode aIN : methodNode.instructions.toArray()){
            sb.append(insnToString(aIN));
        }
        return sb.toString();
    }

    private String insnToString(AbstractInsnNode insn){
        insn.accept(mp);
        StringWriter sw = new StringWriter();
        printer.print(new PrintWriter(sw));
        printer.getText().clear();
        return sw.toString();
    }

    public JarTreeModel getModel() {
        return model;
    }

    public void setModel(JarTreeModel model) {
        fileTree.setModel(model);
        this.model = model;
        fileTree.repaint();
    }

    public JTree getFileTree() {
        return fileTree;
    }

    @Override
    public C getOwner() {
        return owner;
    }
}
