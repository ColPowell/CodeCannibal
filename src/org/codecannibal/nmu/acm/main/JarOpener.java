package org.codecannibal.nmu.acm.main;

import com.sun.deploy.util.ArrayUtil;
import com.sun.xml.internal.ws.binding.FeatureListUtil;
import jdk.internal.org.objectweb.asm.tree.*;
import org.codecannibal.nmu.acm.asm.block.impl.ClassBlock;
import org.codecannibal.nmu.acm.asm.block.impl.FieldBlock;
import org.codecannibal.nmu.acm.asm.block.impl.MethodBlock;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.*;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodNode;

import javax.swing.plaf.ListUI;
import java.io.*;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Colton on 9/23/2015.
 */
public class JarOpener {

    public static HashMap<Integer, String> opCodeMap = new HashMap<>();
    private HashMap<String, byte[]> fileByteMap = new HashMap<>();
    private ArrayList<ClassNode> classNodes = new ArrayList<>();
    private HashMap<String,ClassBlock> classBlocks = new HashMap<>();
    public JarOpener(String fileName){
        if(fileName.endsWith("\\.jar")){
            System.out.println("You suck.");
            System.exit(-1);
        }
        else{
            File ourFile = new File(fileName);
            try {
                initializeOpCodeMap();
                loadMaps(ourFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private void initializeOpCodeMap(){
        opCodeMap.put(0,"nop");
        opCodeMap.put(1,"aconst_null");
        opCodeMap.put(2,"iconst_m1");
        opCodeMap.put(3,"iconst_0");
        opCodeMap.put(4,"iconst_1");
        opCodeMap.put(5,"iconst_2");
        opCodeMap.put(6,"iconst_3");
        opCodeMap.put(7,"iconst_4");
        opCodeMap.put(8,"iconst_5");
        opCodeMap.put(9,"lconst_0");
        opCodeMap.put(10,"lconst_1");
        opCodeMap.put(11,"fconst_0");
        opCodeMap.put(12,"fconst_1");
        opCodeMap.put(13,"fconst_2");
        opCodeMap.put(14,"dconst_0");
        opCodeMap.put(15,"dconst_1");
        opCodeMap.put(16,"bipush");
        opCodeMap.put(17,"sipush");
        opCodeMap.put(18,"ldc");
        opCodeMap.put(21,"iload");
        opCodeMap.put(22,"lload");
        opCodeMap.put(23,"fload");
        opCodeMap.put(24,"dload");
        opCodeMap.put(25,"aload");
        opCodeMap.put(46,"iaload");
        opCodeMap.put(47,"laload");
        opCodeMap.put(48,"faload");
        opCodeMap.put(49,"daload");
        opCodeMap.put(50,"aaload");
        opCodeMap.put(51,"baload");
        opCodeMap.put(52,"caload");
        opCodeMap.put(53,"saload");
        opCodeMap.put(54,"istore");
        opCodeMap.put(55,"lstore");
        opCodeMap.put(56,"fstore");
        opCodeMap.put(57,"dstore");
        opCodeMap.put(58,"astore");
        opCodeMap.put(79,"iastore");
        opCodeMap.put(80,"lastore");
        opCodeMap.put(81,"fastore");
        opCodeMap.put(82,"dastore");
        opCodeMap.put(83,"aastore");
        opCodeMap.put(84,"bastore");
        opCodeMap.put(85,"castore");
        opCodeMap.put(86,"sastore");
        opCodeMap.put(87,"pop");
        opCodeMap.put(88,"pop2");
        opCodeMap.put(89,"dup");
        opCodeMap.put(90,"dup_x1");
        opCodeMap.put(91,"dup_x2");
        opCodeMap.put(92,"dup2");
        opCodeMap.put(93,"dup2_x1");
        opCodeMap.put(94,"dup2_x2");
        opCodeMap.put(95,"swap");
        opCodeMap.put(96,"iadd");
        opCodeMap.put(97,"ladd");
        opCodeMap.put(98,"fadd");
        opCodeMap.put(99,"dadd");
        opCodeMap.put(100,"isub");
        opCodeMap.put(101,"lsub");
        opCodeMap.put(102,"fsub");
        opCodeMap.put(103,"dsub");
        opCodeMap.put(104,"imul");
        opCodeMap.put(105,"lmul");
        opCodeMap.put(106,"fmul");
        opCodeMap.put(107,"dmul");
        opCodeMap.put(108,"idiv");
        opCodeMap.put(109,"ldiv");
        opCodeMap.put(110,"fdiv");
        opCodeMap.put(111,"ddiv");
        opCodeMap.put(112,"irem");
        opCodeMap.put(113,"lrem");
        opCodeMap.put(114,"frem");
        opCodeMap.put(115,"drem");
        opCodeMap.put(116,"ineg");
        opCodeMap.put(117,"lneg");
        opCodeMap.put(118,"fneg");
        opCodeMap.put(119,"dneg");
        opCodeMap.put(120,"ishl");
        opCodeMap.put(121,"lshl");
        opCodeMap.put(122,"ishr");
        opCodeMap.put(123,"lshr");
        opCodeMap.put(124,"iushr");
        opCodeMap.put(125,"lushr");
        opCodeMap.put(126,"iand");
        opCodeMap.put(127,"land");
        opCodeMap.put(128,"ior");
        opCodeMap.put(129,"lor");
        opCodeMap.put(130,"ixor");
        opCodeMap.put(131,"lxor");
        opCodeMap.put(132,"iinc");
        opCodeMap.put(133,"i2l");
        opCodeMap.put(134,"i2f");
        opCodeMap.put(135,"i2d");
        opCodeMap.put(136,"l2i");
        opCodeMap.put(137,"l2f");
        opCodeMap.put(138,"l2d");
        opCodeMap.put(139,"f2i");
        opCodeMap.put(140,"f2l");
        opCodeMap.put(141,"f2d");
        opCodeMap.put(142,"d2i");
        opCodeMap.put(143,"d2l");
        opCodeMap.put(144,"d2f");
        opCodeMap.put(145,"i2b");
        opCodeMap.put(146,"i2c");
        opCodeMap.put(147,"i2s");
        opCodeMap.put(148,"lcmp");
        opCodeMap.put(149,"fcmpl");
        opCodeMap.put(150,"fcmpg");
        opCodeMap.put(151,"dcmpl");
        opCodeMap.put(152,"dcmpg");
        opCodeMap.put(153,"ifeq");
        opCodeMap.put(154,"ifne");
        opCodeMap.put(155,"iflt");
        opCodeMap.put(156,"ifge");
        opCodeMap.put(157,"ifgt");
        opCodeMap.put(158,"ifle");
        opCodeMap.put(159,"if_icmpeq");
        opCodeMap.put(160,"if_icmpne");
        opCodeMap.put(161,"if_icmplt");
        opCodeMap.put(162,"if_icmpge");
        opCodeMap.put(163,"if_icmpgt");
        opCodeMap.put(164,"if_icmple");
        opCodeMap.put(165,"if_acmpeq");
        opCodeMap.put(166,"if_acmpne");
        opCodeMap.put(167,"goto");
        opCodeMap.put(168,"jsr");
        opCodeMap.put(169,"ret");
        opCodeMap.put(170,"tableswitch");
        opCodeMap.put(171,"lookupswitch");
        opCodeMap.put(172,"ireturn");
        opCodeMap.put(173,"lreturn");
        opCodeMap.put(174,"freturn");
        opCodeMap.put(175,"dreturn");
        opCodeMap.put(176,"areturn");
        opCodeMap.put(177,"return");
        opCodeMap.put(178,"getstatic");
        opCodeMap.put(179,"putstatic");
        opCodeMap.put(180,"getfield");
        opCodeMap.put(181,"putfield");
        opCodeMap.put(182,"invokevirtual");
        opCodeMap.put(183,"invokespecial");
        opCodeMap.put(184,"invokestatic");
        opCodeMap.put(185,"invokeinterface");
        opCodeMap.put(186,"invokedynamic");
        opCodeMap.put(187,"new");
        opCodeMap.put(188,"newarray");
        opCodeMap.put(189,"anewarray");
        opCodeMap.put(190,"arraylength");
        opCodeMap.put(191,"athrow");
        opCodeMap.put(192,"checkcast");
        opCodeMap.put(193,"instanceof");
        opCodeMap.put(194,"monitorenter");
        opCodeMap.put(195,"monitorexit");
        opCodeMap.put(197,"multianewarray");
        opCodeMap.put(198,"ifnull");
        opCodeMap.put(199,"ifnonnull");
    }

    private void loadMaps(File ourFile) throws FileNotFoundException {
        ZipInputStream ourZip = getZipInputStream(ourFile);
        ZipEntry entry;
        try {
            while((entry = ourZip.getNextEntry()) != null){
                byte[] bytes = getBytes(ourZip);
                String name = entry.getName();
                if(!fileByteMap.containsKey(name)){
                    if(!name.endsWith(".class")){
                        if(entry.isDirectory()){
                        }
                    }
                    else{

                        ClassReader cr = new ClassReader(bytes);
                        ClassNode cn = getNode(bytes);
                        classNodes.add(cn);
                        classBlocks.put(cn.name, new ClassBlock(cn, cr));
                        fileByteMap.put(name, bytes);
                    }
                }
                if(name.endsWith(".class") && !fileByteMap.containsKey(name)){
                    fileByteMap.put(name,bytes);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAllClassNames(){
        List<String> classes = new ArrayList<String>();
        for(String s : classBlocks.keySet()){
            classes.add(s+".class");
        }
        return classes;
    }

    public ZipInputStream getZipInputStream(final File ourFile) throws FileNotFoundException {
        return new ZipInputStream(new FileInputStream(ourFile));
    }

    public void loadFileNamesToMap(final File ourFile) throws FileNotFoundException {
        ZipInputStream ourZip = getZipInputStream(ourFile);
        ZipEntry entry;
        try {
            while((entry = ourZip.getNextEntry()) != null){
                byte[] bytes = getBytes(ourZip);
                String name = entry.getName();
                System.out.println("file getName thing: " + name);
                if(!fileByteMap.containsKey(name)){
                    if(!name.endsWith(".class")){
                        if(entry.isDirectory()){
                        }
                    }
                    else{
                        ClassNode cn = getNode(bytes);
                        classNodes.add(cn);
                        fileByteMap.put(name, bytes);
                    }
                }
                if(name.endsWith(".class") && !fileByteMap.containsKey(name)){
                    fileByteMap.put(name,bytes);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getBytesForClassName(String className){
        return fileByteMap.get(className+".class");
    }

    public void printFileNames(){
        for(String key : fileByteMap.keySet()){
            System.out.println(key);
        }
    }

    private String modifiers(int mod){
        return Modifier.toString(mod);
    }
    private String getParameters(MethodNode mn){
        String params = "";
        String desc = mn.desc.split("\\)")[0];
        desc = desc.substring(1);
        if(!desc.equals("")){
            int varPlace = 0;
            String arrays = "";
            for(int i = 0; i < desc.length(); i++){
                char c = desc.charAt(i);
                switch(c){
                    case 'I':
                        params+= "final int"+arrays+" " + mn.localVariables.get(varPlace).name+", ";
                        varPlace++;
                        arrays = "";
                        break;
                    case 'B':
                        params+= "final byte"+arrays+" " + mn.localVariables.get(varPlace).name+", ";
                        varPlace++;
                        arrays = "";
                        break;
                    case 'Z':
                        params+= "final boolean"+arrays+" " + mn.localVariables.get(varPlace).name+", ";
                        varPlace++;
                        arrays = "";
                        break;
                    case 'J':
                        params+= "final long"+arrays+" " + mn.localVariables.get(varPlace).name+", ";
                        varPlace++;
                        arrays = "";
                        break;
                    case 'C':
                        params+= "final char"+arrays+" " + mn.localVariables.get(varPlace).name+", ";
                        varPlace++;
                        arrays = "";
                        break;
                    case 'S':
                        params+= "final short"+arrays+" " + mn.localVariables.get(varPlace).name+", ";
                        varPlace++;
                        arrays = "";
                        break;
                    case 'F':
                        params+= "final float"+arrays+" " + mn.localVariables.get(varPlace).name+", ";
                        varPlace++;
                        arrays = "";
                        break;
                    case 'D':
                        params+= "final double"+arrays+" " + mn.localVariables.get(varPlace).name+", ";
                        varPlace++;
                        arrays = "";
                        break;
                    case 'L':
                        String tmp = desc.substring(i+1);
                        String myClass = tmp.split(";")[0];
                        params+="final " + myClass+arrays + " " + mn.localVariables.get(varPlace).name+", ";
                        varPlace++;
                        i+=myClass.length();
                        arrays = "";
                        break;
                    case '[':
                        arrays+="[]";
                        break;
                }
            }
        }
        if(params.length() > 0){
            params = params.substring(0, params.length()-2);
        }
        return params;
    }
    private String getPackageName(ClassNode cn){
        String[] fullName = cn.name.split("/");
        String packageName = "";
        if(fullName != null && fullName.length > 0){
            packageName+="package ";
            for(int i = 0; i < fullName.length-1; i++){
                packageName += fullName[i];
                if(i == fullName.length-2){
                    packageName+=";";
                }
                else{
                    packageName+=".";
                }
            }
        }
        if(packageName.equals("package "))
            return "";
        return packageName;
    }

    public void printClassByteCode(String className){
        for(ClassBlock cn : classBlocks.values()){
            if(cn.getName().contains(className)){
                System.out.println(modifiers(cn.getAccess()) + " class " + cn.getName());
                String tabs = "    ";
                for(FieldBlock fb : cn.getFields()){
                    String ourHash = "opCodeMap.put(";
                    ourHash+=fb.getNode().value + ",\"" +fb.getName().toLowerCase()+"\");";
                    System.out.println(ourHash);
                    //System.out.println(tabs + modifiers(fb.getAccess()) + " " + fb.getNode().desc +" " + fb.getName() + " " + fb.getNode().value);
                }
                for(MethodBlock mb : cn.getMethods()){
                    if(mb.getName().contains("clinit")){
                        System.out.println(tabs+ "clinit{");
                        tabs+="    ";
                        for(AbstractInsnNode ain : mb.getInstructions()){
                            System.out.println(tabs+ ain.toString());
                        }
                        tabs = "    ";
                        System.out.println("}");
                    }
                }
            }
        }
    }

    public void printClassNodeStuff(){
        for(ClassBlock cn : classBlocks.values()){
            if(cn.getName().contains("asm"))
                continue;
            System.out.println(getPackageName(cn.getNode()) + "\n");

            System.out.println(modifiers(cn.getAccess())+" class "+cn.getName().split("/")[cn.getName().split("/").length-1]+" "+(cn.getNode().outerClass != null ? "extends " + cn.getNode().outerClass : "")+" "+ ArrayUtil.arrayToString(cn.getNode().interfaces.toArray(new String[cn.getNode().interfaces.size()])) + "{");
//            System.out.println("Class getName: " + cn.getName);
            for(MethodBlock mb : cn.getMethods()){
                boolean inLabel = false;
                System.out.println("    "+modifiers(mb.getAccess())+" "+mb.getName() + "(" + mb.getPrettyParameters()+"){ //"+mb.getDescription()+":"+mb.getNode().signature);

                for(AbstractInsnNode ain : mb.getInstructions()){
                    if(ain instanceof LineNumberNode || ain instanceof FrameNode)
                        continue;
                    if(ain instanceof LabelNode){
                        if(inLabel){
                            System.out.println("        }");
                        }
                        else{
                            inLabel = true;
                        }
                        System.out.println("        " + ain.toString());

                    }
                    else
                        System.out.println("            " + ain.toString());
                }
                System.out.println("        }");
                System.out.println("    }");
            }
            System.out.println("}");
        }
    }


    public List<String> getPrettyByteCodeList(String className){
        ClassBlock cn = classBlocks.get(className);
        System.out.println(cn.getMethods().size());
        List<List<String>> listoflists = new ArrayList<>();
        for(int i = 0; i < cn.getMethods().size()+1; i++){
            listoflists.add(null);
        }
        System.out.println(listoflists.size());
        List<String> prettyByteCode1 = new ArrayList<String>();

        //for(ClassBlock cn : classBlocks.values()){
          //  if(!cn.getName().contains(className))
            //    continue;
           prettyByteCode1.add(getPackageName(cn.getNode()) + "\n\n");

            prettyByteCode1.add(modifiers(cn.getAccess()) + " class " + cn.getName().split("/")[cn.getName().split("/").length - 1] + " " + (cn.getNode().outerClass != null ? "extends " + cn.getNode().outerClass : "") + " " + ArrayUtil.arrayToString(cn.getNode().interfaces.toArray(new String[cn.getNode().interfaces.size()])) + "{\n");
//            System.out.println("Class getName: " + cn.getName);
        listoflists.add(0,prettyByteCode1);
        List<Thread> threads = new ArrayList<>();
        //String[] methodStrings
        int methodsPlace = 1;
        for(MethodBlock mb : cn.getMethods()){
            int methodsPlace2 = methodsPlace;
                Runnable runMethodStuff = new Runnable(){
                    public void run(){
                        int realMethodsPlace = methodsPlace2;
                        List<String> prettyByteCode = new ArrayList<String>();
                        System.out.println("Running method: " + mb.getName() + " at " + realMethodsPlace);
                        boolean inLabel = false;
                        prettyByteCode.add("    " + modifiers(mb.getAccess()) + " " + mb.getName() + "(" + mb.getPrettyParameters() + "){ //" + mb.getDescription() + ":" + mb.getNode().signature + "\n");

                        for(AbstractInsnNode ain : mb.getInstructions()){
                            if(ain instanceof LineNumberNode || ain instanceof FrameNode)
                                continue;
                            if(ain instanceof LabelNode){
                                if(inLabel){
                                    prettyByteCode.add("        }\n");
                                }
                                else{
                                    inLabel = true;
                                }
                                prettyByteCode.add("        " + ain.toString() + "\n");

                            }
                            else
                                prettyByteCode.add("            " + ain.toString() + "\n");
                        }
                        prettyByteCode.add("        }\n");
                        prettyByteCode.add("    }\n");

                        listoflists.add(realMethodsPlace, prettyByteCode);
                    }
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
    /**
     *
     */
    public byte[] getBytes(final InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int a = 0;
        while ((a = is.read(buffer)) != -1) {
            baos.write(buffer, 0, a);
        }
        baos.close();
        return baos.toByteArray();
    }

    public static ClassNode getNode(final byte[] bytez) {
        ClassReader cr = new ClassReader(bytez);
        ClassNode cn = new ClassNode();
        try {
            cr.accept(cn, ClassReader.EXPAND_FRAMES);
        } catch (Exception e) {
            try {
                cr.accept(cn, ClassReader.SKIP_FRAMES);
            } catch(Exception e2) {
                e2.printStackTrace(); //just skip it
            }
        }
        cr = null;
        return cn;
    }
}
