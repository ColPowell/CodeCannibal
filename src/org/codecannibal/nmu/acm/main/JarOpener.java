package org.codecannibal.nmu.acm.main;

import org.codecannibal.nmu.acm.asm.block.impl.ClassBlock;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Colton on 9/23/2015.
 */
public class JarOpener {

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
                loadMaps(ourFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public HashMap<String, ClassBlock> getClassBlocks(){
        return this.classBlocks;
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
