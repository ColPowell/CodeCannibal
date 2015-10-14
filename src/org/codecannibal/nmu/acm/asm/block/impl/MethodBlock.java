package org.codecannibal.nmu.acm.asm.block.impl;

import org.codecannibal.nmu.acm.asm.block.AbstractBlock;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Colton on 9/29/2015.
 */
public class MethodBlock implements AbstractBlock {

    private MethodNode node;
    private ClassBlock owner;
    private List<AbstractInsnNode> instructions = new ArrayList<>();
    public MethodBlock(MethodNode node, ClassBlock parent){
        this.node = node;
        this.owner = parent;
    }

    public MethodNode getNode(){
        return this.node;
    }

    @Override
    public String getName() {
        return node.name;
    }

    @Override
    public int getAccess() {
        return node.access;
    }

    public ClassBlock getOwner(){
        return this.owner;
    }

    public String getDescription(){
        return node.desc;
    }

    public String getRawParameters(){
        return getDescription().split("\\)")[0].substring(1);
    }

    public String[] getParameters(){
        List<String> params = new ArrayList<>();
        String desc = getRawParameters();
        if(!desc.equals("")){
            int varPlace = 0;
            String arrays = "";
            for(int i = 0; i < desc.length(); i++){
                String currParam = "";
                char c = desc.charAt(i);
                switch(c){
                    case 'I':
                        currParam = "final int"+arrays;
                        break;
                    case 'B':
                        currParam = "final byte"+arrays;
                        break;
                    case 'Z':
                        currParam = "final boolean"+arrays;
                        break;
                    case 'J':
                        currParam = "final long"+arrays;
                        break;
                    case 'C':
                        currParam = "final char"+arrays;
                        break;
                    case 'S':
                        currParam = "final short"+arrays;
                        break;
                    case 'F':
                        currParam= "final float"+arrays;
                        break;
                    case 'D':
                        currParam= "final double"+arrays;
                        break;
                    case 'L':
                        String tmp = desc.substring(i+1);
                        String myClass = tmp.split(";")[0];
                        currParam="final " + myClass+arrays;
                        i+=myClass.length();
                        break;
                    case '[':
                        arrays+="[]";
                        break;
                }
                if(!currParam.equals("") && node != null && node.localVariables != null && node.localVariables.size() > varPlace) {
                    currParam+=" ";
                    currParam+=node.localVariables.get(varPlace).name;
                    params.add(currParam);
                    varPlace++;
                    arrays = "";
                }
            }
        }
        return params.toArray(new String[params.size()]);
    }
    public String getPrettyParameters(){
        String[] params = getParameters();
        String prettyParams = "";
        if(params.length > 0){
            for(String s : params){
                prettyParams+=s+", ";
            }
            prettyParams = prettyParams.substring(0,prettyParams.length()-2);
        }
        return prettyParams;
    }

    public List<AbstractInsnNode> getInstructions(){
        if(instructions.isEmpty()){
            for(AbstractInsnNode ain : node.instructions.toArray()){
                instructions.add(ain);
            }
        }
        return instructions;
    }
}
