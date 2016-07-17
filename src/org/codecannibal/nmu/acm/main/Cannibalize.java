package org.codecannibal.nmu.acm.main;

import com.sun.deploy.util.ArrayUtil;
import org.codecannibal.nmu.acm.asm.block.impl.ClassBlock;
import org.codecannibal.nmu.acm.asm.block.impl.MethodBlock;
import org.dreambot.decomplier.Decompiler;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.util.Printer;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * Created by Colton on 11/19/2015.
 */
public class Cannibalize {

	public static HashMap<Integer, String> opCodeMap = new HashMap<>();
	private JarOpener jar;


	public Cannibalize(JarOpener jar){
		this.jar = jar;
		initializeOpCodeMap();
	}

	private void initializeOpCodeMap(){
		for(int i = 0; i < 200; i++){
			opCodeMap.put(i, Printer.OPCODES[i].toLowerCase());
		}
	}

	private String modifiers(int mod){
		return Modifier.toString(mod);
	}

	private String getPackageName(ClassNode cn){
		String[] fullName = cn.name.split("/");
		String packageName = "";
		if(fullName != null && fullName.length > 0){
			packageName += "package ";
			for(int i = 0; i < fullName.length - 1; i++){
				packageName += fullName[i];
				if(i == fullName.length - 2){
					packageName += ";";
				} else{
					packageName += ".";
				}
			}
		}
		if(packageName.equals("package "))
			return "";
		return packageName;
	}

	public void printClassNodeStuff(){
		for(ClassBlock cn : jar.getClassBlocks().values()){
			if(cn.getName().contains("asm"))
				continue;
			System.out.println(getPackageName(cn.getNode()) + "\n");

			System.out.println(modifiers(cn.getAccess()) + " class " + cn.getName().split("/")[cn.getName().split("/").length - 1] + " " + (cn.getNode().outerClass != null ? "extends " + cn.getNode().outerClass : "") + " " + ArrayUtil.arrayToString(cn.getNode().interfaces.toArray(new String[cn.getNode().interfaces.size()])) + "{");
//            System.out.println("Class getName: " + cn.getName);
			for(MethodBlock mb : cn.getMethods()){
				boolean inLabel = false;
				System.out.println("    " + modifiers(mb.getAccess()) + " " + mb.getName() + "(" + mb.getPrettyParameters() + "){ //" + mb.getDescription() + ":" + mb.getNode().signature);

				for(AbstractInsnNode ain : mb.getInstructions()){
					if(ain instanceof LineNumberNode || ain instanceof FrameNode)
						continue;
					if(ain instanceof LabelNode){
						if(inLabel){
							System.out.println("        }");
						} else{
							inLabel = true;
						}
						System.out.println("        " + ain.getPrettyByteCode());

					} else
						System.out.println("            " + ain.getPrettyByteCode());
				}
				System.out.println("        }");
				System.out.println("    }");
			}
			System.out.println("}");
		}
	}

	private String getImplements(ClassBlock cn){
		String inters = "";
		if(cn.getNode().interfaces.size() >0){
			for(int i = 0; i < cn.getNode().interfaces.size(); i++){
				inters+=cn.getNode().interfaces.get(i);
				if(i < cn.getNode().interfaces.size()-1){
					inters+=", ";
				}
			}
		}
		return inters;
	}

	private String getJavaImplements(ClassBlock cn){
		String inters = "";
		if(cn.getNode().interfaces.size() >0){
			for(int i = 0; i < cn.getNode().interfaces.size(); i++){
				String[] data = cn.getNode().interfaces.get(i).split("/");
				inters+=data[data.length-1];
				if(i < cn.getNode().interfaces.size()-1){
					inters+=", ";
				}
			}
		}
		return inters;
	}


	public List<String> getPrettyByteCodeList(String className){
		ClassBlock cn = Decompiler.getOpener().getClassBlocks().get(className);
		List<List<String>> listoflists = new ArrayList<>();
		for(int i = 0; i < cn.getMethods().size() + 1; i++){
			listoflists.add(null);
		}

		List<String> prettyByteCode1 = new ArrayList<String>();

		//for(ClassBlock cn : classBlocks.values()){
		//  if(!cn.getName().contains(className))
		//    continue;
		if(!getPackageName(cn.getNode()).isEmpty())
			prettyByteCode1.add(getPackageName(cn.getNode()) + "\n\n");
		getImplements(cn);
		prettyByteCode1.add(modifiers(cn.getAccess()) + " class " + cn.getName().split("/")[cn.getName().split("/").length - 1] + " " + (!cn.getNode().superName.contains("Object") ? "extends " + cn.getNode().superName : "") + " " + (cn.getNode().interfaces.size() > 0 ? "implements " + getImplements(cn) : "") + "{\n");
//            System.out.println("Class getName: " + cn.getName);
		listoflists.add(0, prettyByteCode1);
		List<Thread> threads = new ArrayList<>();
		//String[] methodStrings
		int methodsPlace = 1;
		for(MethodBlock mb : cn.getMethods()){
			int methodsPlace2 = methodsPlace;
			Runnable runMethodStuff = ()->{
				String tabs = "\t";
				int realMethodsPlace = methodsPlace2;
				List<String> prettyByteCode = new ArrayList<>();
				boolean inLabel = false;
				String name = mb.getName();
				boolean init = false;
				if(name.equals("<init>")){
					init = true;
					name = cn.getNode().name;
				}
				prettyByteCode.add(tabs + modifiers(mb.getAccess()) + " " + name + "(" + mb.getPrettyParameters() + "){ //"+(init ? " <init> //" : "") + mb.getDescription() + ":" + mb.getNode().signature + "\n");
				tabs += "\t";
				for(AbstractInsnNode ain : mb.getInstructions()){
					if(ain instanceof LineNumberNode || ain instanceof FrameNode){
						if(ain instanceof LineNumberNode){
							prettyByteCode.add(tabs + "LN "+((LineNumberNode) ain).getPrettyByteCode()+"\n");
							//prettyByteCode.add("\n");
						} else{
							//prettyByteCode.add(((FrameNode) ain).toString());
							//prettyByteCode.add("\n");
						}
						continue;
					}
					if(ain instanceof LabelNode){
						if(inLabel){
							prettyByteCode.add(tabs + "}\n");
						} else{
							inLabel = true;
						}
						prettyByteCode.add(tabs + ain.getPrettyByteCode() + "\n");

					} else
						prettyByteCode.add(tabs + "\t" + ain.getPrettyByteCode() + "\n");
				}
				prettyByteCode.add(tabs + "}\n");
				tabs = "\t";
				prettyByteCode.add(tabs + "}\n");

				listoflists.add(realMethodsPlace, prettyByteCode);
			};
			Thread methodThread = new Thread(runMethodStuff);
			threads.add(methodThread);
			methodThread.start();
			methodsPlace++;
		}
		//}
		while(!threads.isEmpty()){
			if(!threads.get(0).isAlive()){
				threads.remove(0);
			}
		}
		List<String> prettyByteCode = new ArrayList<>();
		for(List<String> list : listoflists){
			if(list == null)
				continue;
			for(String s : list){
				prettyByteCode.add(s);
			}
		}
		prettyByteCode.add("}\n");
		return prettyByteCode;
	}

	public String getMethodDeclaration(MethodBlock mb){
		//<init>(){ //(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V:null
		//(tabs + modifiers(mb.getAccess()) + " " + mb.getName() + "(" + mb.getPrettyParameters() + "){ //" + mb.getDescription() + ":" + mb.getNode().signature + "\n");
		//aa(final int n, final String h, final String r, final String a) {
		String decl = modifiers(mb.getAccess())+" " + "";

		return decl;

	}

	public static Stack<AbstractInsnNode> opcodeStack = new Stack<>();

	public List<String> getPrettyJavaCodeList(String className){
		OpcodeVisitor visitor = new OpcodeVisitor(Opcodes.ASM5);
		List<String> prettyJavaCode = new ArrayList<>();
		ClassBlock cn = Decompiler.getOpener().getClassBlocks().get(className);
		List<List<String>> listoflists = new ArrayList<>();
		for(int i = 0; i < cn.getMethods().size() + 1; i++){
			listoflists.add(null);
		}
		if(!getPackageName(cn.getNode()).isEmpty())
			prettyJavaCode.add(getPackageName(cn.getNode()) + "\n\n");


		prettyJavaCode.add(modifiers(cn.getAccess()) + " class " + cn.getName().split("/")[cn.getName().split("/").length - 1] + " " + (!cn.getNode().superName.contains("Object") ? "extends " + cn.getNode().superName : "") + " " + getJavaImplements(cn) + "{\n");
		listoflists.add(0, prettyJavaCode);
		List<Thread> threads = new ArrayList<>();
		//String[] methodStrings
		int methodsPlace = 1;
		for(MethodBlock mb : cn.getMethods()){
			int methodsPlace2 = methodsPlace;
			String tabs = "\t";
			int realMethodsPlace = methodsPlace2;
			List<String> prettyByteCode = new ArrayList<>();
			boolean inLabel = false;
			String name = mb.getName();
			if(name.equals("<init>")){
				if(cn.getNode().name.contains("/")){
					name = cn.getNode().name.split("/")[cn.getNode().name.split("/").length-1];
				}
				else
					name = cn.getNode().name;
			}
			prettyByteCode.add(tabs + modifiers(mb.getAccess()) + " " + name + "(" + mb.getPrettyJavaParameters() + "){ \n");////" +(init ? " <init> //" : "")+ mb.getDescription() + ":" + mb.getNode().signature + "\n");
			tabs += "\t";
			for(AbstractInsnNode ain : mb.getInstructions()){

				if(ain instanceof LineNumberNode || ain instanceof FrameNode){
					if(ain instanceof LineNumberNode){
					} else{
					}
					continue;
				}
				if(ain instanceof LabelNode){
					if(inLabel){
						//prettyByteCode.add(tabs + "}\n");
					} else{
						//inLabel = true;
					}
					//prettyByteCode.add(tabs + ain.getPrettyByteCode() + "\n");

				} else{
					opcodeStack.push(ain);
					ain.accept(visitor);
					prettyByteCode.add(tabs + ain.toString() + "\n");
				}
			}
			prettyByteCode.add(tabs + "}\n");
			tabs = "\t";
			prettyByteCode.add(tabs + "}\n");

			listoflists.add(realMethodsPlace, prettyByteCode);
			//};
			//Thread methodThread = new Thread(runMethodStuff);
			//threads.add(methodThread);
			//methodThread.start();
			methodsPlace++;
			prettyJavaCode.addAll(prettyByteCode);
			prettyJavaCode.add("\n");
		}
		//}
		/*while(!threads.isEmpty()){
			if(!threads.get(0).isAlive()){
				threads.remove(0);
			}
		}*/

		/*for(List<String> list : listoflists){
			if(list == null)
				continue;
			for(String s : list){
				prettyByteCode.add(s);
			}
		}*/
		prettyJavaCode.add("}\n");
		return prettyJavaCode;
	}

}
