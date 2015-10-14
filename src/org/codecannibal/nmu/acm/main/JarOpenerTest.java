package org.codecannibal.nmu.acm.main;

/**
 * Created by Colton on 9/23/2015.
 */
public class JarOpenerTest {

    public static void main(String[] args){
        String testFile = "C:\\Users\\Colton\\IdeaProjects\\CodeCannibal\\out\\artifacts\\testJar\\testJar.jar";
        JarOpener jarOpener = new JarOpener(testFile);
        //jarOpener.printClassByteCode("Opcodes");
        jarOpener.printFileNames();
        jarOpener.printClassNodeStuff();
        //byte[] clientBytes = jarOpener.getBytesForClassName("org/codecannibal/nmu/acm/main/JarOpenerTest");
        //jarOpener.printFileNames();
//        System.out.println(jarOpener.getNextByte());
        /*while(jarOpener.hasNext()){
            //18 is ldc, 1 parameter
            if(jarOpener.getNextByte() == 18){
                System.out.println("LDC: " + jarOpener.getNextByte());
                break;
            }
        }*/

    }
}
