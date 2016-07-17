package org.codecannibal.nmu.acm.main;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Colton on 7/17/2016.
 */
public class OpcodeVisitor extends MethodVisitor {


	private List<String> codeList = new ArrayList<>();


	public OpcodeVisitor(int api){
		super(api);
	}

	@Override
	public void visitParameter(String name, int access){
		super.visitParameter(name, access);
	}

	@Override
	public AnnotationVisitor visitAnnotationDefault(){
		return super.visitAnnotationDefault();
	}

	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible){
		return super.visitAnnotation(desc, visible);
	}

	@Override
	public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible){
		return super.visitTypeAnnotation(typeRef, typePath, desc, visible);
	}

	@Override
	public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible){
		return super.visitParameterAnnotation(parameter, desc, visible);
	}

	@Override
	public void visitAttribute(Attribute attr){
		super.visitAttribute(attr);
	}

	@Override
	public void visitCode(){
		super.visitCode();
	}

	@Override
	public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack){
		super.visitFrame(type, nLocal, local, nStack, stack);
	}

	@Override
	public void visitInsn(int opcode){
		super.visitInsn(opcode);
	}

	@Override
	public void visitIntInsn(int opcode, int operand){
		super.visitIntInsn(opcode, operand);
	}

	@Override
	public void visitVarInsn(int opcode, int var){
		super.visitVarInsn(opcode, var);
	}

	@Override
	public void visitTypeInsn(int opcode, String type){
		super.visitTypeInsn(opcode, type);
	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc){
		super.visitFieldInsn(opcode, owner, name, desc);
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc){
		super.visitMethodInsn(opcode, owner, name, desc);
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf){
		String[] params = getParameters(desc);
		System.out.println("Param count: " + params.length);
		MethodInsnNode methodNode = (MethodInsnNode)Cannibalize.opcodeStack.pop();
		String methodParams = "(";
		for(int i =0; i < params.length; i++){
			AbstractInsnNode ain = Cannibalize.opcodeStack.pop();
			if(ain instanceof FieldInsnNode){
				methodParams+=((FieldInsnNode) ain).name;
			}
			System.out.println(ain.toString());
			System.out.println(ain.getPrettyByteCode());
			//methodParams+=params[i];
			if(i < params.length-1){
				methodParams+=", ";
			}
		}
		methodParams+=");";
		String methodOwner = owner;
		System.out.println(owner+":"+methodNode.owner);
		if(owner.equals(methodNode.owner)){
			methodOwner = "this";
		}
		if(opcode == Opcodes.INVOKEVIRTUAL){
		}
		String methodCall = methodOwner+"."+name+methodParams;
		System.out.println("Here's my built up one: " + methodCall);
		System.out.println("Visiting method instruction node: "+Cannibalize.opCodeMap.get(opcode)+">>>" + owner +"." +name+":"+desc+":"+itf);
		super.visitMethodInsn(opcode, owner, name, desc, itf);
	}

	@Override
	public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs){
		super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
	}

	@Override
	public void visitJumpInsn(int opcode, Label label){
		super.visitJumpInsn(opcode, label);
	}

	@Override
	public void visitLabel(Label label){
		super.visitLabel(label);
	}

	@Override
	public void visitLdcInsn(Object cst){
		super.visitLdcInsn(cst);
	}

	@Override
	public void visitIincInsn(int var, int increment){
		super.visitIincInsn(var, increment);
	}

	@Override
	public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels){
		super.visitTableSwitchInsn(min, max, dflt, labels);
	}

	@Override
	public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels){
		super.visitLookupSwitchInsn(dflt, keys, labels);
	}

	@Override
	public void visitMultiANewArrayInsn(String desc, int dims){
		super.visitMultiANewArrayInsn(desc, dims);
	}

	@Override
	public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String desc, boolean visible){
		return super.visitInsnAnnotation(typeRef, typePath, desc, visible);
	}

	@Override
	public void visitTryCatchBlock(Label start, Label end, Label handler, String type){
		super.visitTryCatchBlock(start, end, handler, type);
	}

	@Override
	public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String desc, boolean visible){
		return super.visitTryCatchAnnotation(typeRef, typePath, desc, visible);
	}

	@Override
	public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index){
		super.visitLocalVariable(name, desc, signature, start, end, index);
	}

	@Override
	public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String desc, boolean visible){
		return super.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, desc, visible);
	}

	@Override
	public void visitLineNumber(int line, Label start){
		super.visitLineNumber(line, start);
	}

	@Override
	public void visitMaxs(int maxStack, int maxLocals){
		super.visitMaxs(maxStack, maxLocals);
	}

	@Override
	public void visitEnd(){
		super.visitEnd();
	}



	public String[] getParameters(String desc){
		List<String> params = new ArrayList<>();
		if(!desc.equals("") && !desc.equals("()")){
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
}
