package org.codecannibal.nmu.acm.asm.block.impl;

import org.codecannibal.nmu.acm.asm.block.AbstractBlock;
import org.objectweb.asm.tree.FieldNode;

/**
 * Created by Colton on 9/29/2015.
 */
public class FieldBlock implements AbstractBlock {

    private ClassBlock owner;
    private FieldNode node;
    public FieldBlock(FieldNode node, ClassBlock owner){
        this.owner = owner;
        this.node = node;
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
        return owner;
    }

    public FieldNode getNode(){
        return node;
    }
}
