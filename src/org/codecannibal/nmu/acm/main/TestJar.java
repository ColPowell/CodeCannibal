package org.codecannibal.nmu.acm.main;
public class TestJar {
    public static int a = 1;
    public int b = 2;
    public static int someMethod(){
        TestJar jar2 = new TestJar();
        TestJar jar = new TestJar();
        int x = 1038453453;
        int y = x*-123049613;
        long z = 1283746123857L;
        float f = 86351;
        double d = 86351243.33;
        if(x < y){
            x = y;
        }
        else{
            y = x;
        }
        jar2.testMethod();

        x = TestJar.a + jar.b;
        System.out.println(x+":"+y);
        jar.testMethod();
        jar.otherMethod();
        return x*y;
    }
    public int testMethod(){
       return 1;
    }
    public void otherMethod(){

    }
    private static void otherMethod(int a, int b){
        System.out.println("Hello");
    }
    public static String testParams(String[][][][] yes,boolean three,float f, short s, double d, TestJar jar, char a, byte b, long c, int...ints){
        System.out.println("oh?");
        return "";
    }
}
