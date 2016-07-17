package org.dreambot.decomplier.gui.component.impl.center.impl;

import org.dreambot.decomplier.gui.component.DComponent;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

/**
 * @author Notorious BPP
 * @since 7/16/2016.
 */
public class DTextPane<C extends Component> extends JTextPane implements DComponent<C> {

    private final C owner;

    public DTextPane(C owner) {
        this.owner = owner;

        init(owner);
    }

    public String[] blueKeywords = new String[]{"class","interface","extends","implements","public","private","synchronized","abstract","protected","return","new","throw","final","this","try","catch","static","void"};
    public String[] primitiveWords = new String[]{"byte","int","long","short","double","float","char","boolean"};


    private int findFirstNonWordChar (String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
            index++;
        }
        return index;
    }

    private String getBlueKeywords(){
        String s = "";
        for(String keyword : blueKeywords){
            s+=keyword;
            s+="|";
        }
        s = s.substring(0,s.length()-1);
        return s;
    }

    private String getPrimitiveKeywords(){
        String s = "";
        for(String keyword : primitiveWords){
            s+=keyword;
            s+="|";
        }
        s = s.substring(0,s.length()-1);
        return s;
    }

    private int findLastNonWordChar (String text, int index) {
        while (--index >= 0) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
        }
        return index;
    }

    private void init(C owner) {
        Dimension dimension = new Dimension(owner.getWidth() - 256, owner.getHeight() - 25);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
        TabStop[] tabs = new TabStop[4];
        tabs[0] = new TabStop(20);
        tabs[1] = new TabStop(40);
        tabs[2] = new TabStop(60);
        tabs[3] = new TabStop(80);
        TabSet tabset = new TabSet(tabs);


        final StyleContext cont = StyleContext.getDefaultStyleContext();
        AttributeSet aset = cont.addAttribute(SimpleAttributeSet.EMPTY,
                StyleConstants.TabSet, tabset);
        setParagraphAttributes(aset, false);
        this.setFont(getFont().deriveFont(Font.BOLD));
        final AttributeSet modifierAttr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLUE);
        final AttributeSet primitiveAttr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.green);
        final AttributeSet stringAttr = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.ORANGE);
        final AttributeSet attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
        /*DefaultStyledDocument doc = new DefaultStyledDocument() {
            public void insertString (int offset, String str, AttributeSet a) throws BadLocationException {
                super.insertString(offset, str, a);

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offset);
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, offset + str.length());
                int wordL = before;
                int wordR = before;

                while (wordR <= after) {
                    if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
                        if (text.substring(wordL, wordR).matches("(\\W)*("+getBlueKeywords()+")"))
                            setCharacterAttributes(wordL, wordR - wordL, modifierAttr, false);
                        else if (text.substring(wordL, wordR).matches("(\\W)*("+getPrimitiveKeywords()+")"))
                            setCharacterAttributes(wordL, wordR - wordL, primitiveAttr, false);
                        else if (text.substring(wordL, wordR).matches("(\\W)*(String)"))
                            setCharacterAttributes(wordL, wordR - wordL, stringAttr, false);
                        else
                            setCharacterAttributes(wordL, wordR - wordL, attrBlack, false);
                        wordL = wordR;
                    }
                    wordR++;
                }
            }

            public void remove (int offs, int len) throws BadLocationException {
                super.remove(offs, len);

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offs);
                if (before < 0) before = 0;
                int after = findFirstNonWordChar(text, offs);
                if (text.substring(before, after).matches("(\\W)*("+getBlueKeywords()+")")) {
                    setCharacterAttributes(before, after - before, modifierAttr, false);
                }
                else if (text.substring(before, after).matches("(\\W)*("+getPrimitiveKeywords()+")"))
                    setCharacterAttributes(before, after - before, primitiveAttr, false);
                else if (text.substring(before, after).matches("(\\W)*(String)"))
                    setCharacterAttributes(before, after - before, primitiveAttr, false);
                else {
                    setCharacterAttributes(before, after - before, attrBlack, false);
                }
            }
        };
        this.setDocument(doc);*/
    }

    @Override
    public C getOwner() {
        return owner;
    }
}
