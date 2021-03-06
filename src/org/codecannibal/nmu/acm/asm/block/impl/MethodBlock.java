package org.codecannibal.nmu.acm.asm.block.impl;

import org.codecannibal.nmu.acm.asm.block.AbstractBlock;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LocalVariableNode;
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
                        currParam = "int"+arrays;
                        break;
                    case 'B':
                        currParam = "byte"+arrays;
                        break;
                    case 'Z':
                        currParam = "boolean"+arrays;
                        break;
                    case 'J':
                        currParam = "long"+arrays;
                        break;
                    case 'C':
                        currParam = "char"+arrays;
                        break;
                    case 'S':
                        currParam = "short"+arrays;
                        break;
                    case 'F':
                        currParam= "float"+arrays;
                        break;
                    case 'D':
                        currParam= "double"+arrays;
                        break;
                    case 'L':
                        String tmp = desc.substring(i+1);
                        String myClass = tmp.split(";")[0];
                        myClass = myClass.replaceAll("/","\\.");
                        currParam=myClass+arrays;
                        i+=myClass.length();
                        break;
                    case '[':
                        arrays+="[]";
                        break;
                }
                if(!currParam.equals("")){
                    currParam+=" ";
                    currParam+="var"+varPlace;
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

    public String getPrettyJavaParameters(){
        String[] params = getParameters();
        String prettyParams = "";
        if(params.length > 0){
            for(String s : params){
                if(s.contains("/")){
                    String[] data = s.split("\\.");
                    prettyParams+=data[data.length-1]+", ";
                }
                else
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
