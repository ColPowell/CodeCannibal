package org.codecannibal.nmu.acm.asm.block.impl;

import org.codecannibal.nmu.acm.asm.block.AbstractBlock;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Colton on 9/29/2015.
 */
public class ClassBlock implements AbstractBlock{

    private ClassReader reader;
    private ClassNode node;
    private List<MethodBlock> methods = new ArrayList<>();
    private List<FieldBlock> fields = new ArrayList<>();
    public ClassBlock(ClassNode node, ClassReader reader){
        this.node = node;
        this.reader = reader;
    }
    public ClassNode getNode(){
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

    public List<MethodBlock> getMethods(){
        if(methods.isEmpty() ){
            for(MethodNode mn : node.methods){
                methods.add(new MethodBlock(mn, this));
            }
        }
        return methods;
    }
    public List<FieldBlock> getFields(){
        if(fields.isEmpty()){
            for(FieldNode fn : node.fields){
                fields.add(new FieldBlock(fn, this));
            }
        }

        return fields;
    }

    @Override
    public String toString(){
        return "class " + getName();
    }
}
