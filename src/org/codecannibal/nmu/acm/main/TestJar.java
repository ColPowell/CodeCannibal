package org.codecannibal.nmu.acm.main;
public class TestJar {
    public static int a = 1;
    public int b = 2;
    public static int someMethod(){
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

        x = TestJar.a + jar.b;
        System.out.println(x+":"+y);
        return x*y;
    }
    private static void otherMethod(int a, int b){
        System.out.println("Hello");
    }
    public static String testParams(String[][][][] yes,boolean three,float f, short s, double d, TestJar jar, char a, byte b, long c, int...ints){
        System.out.println("oh?");
        return "";
    }
}
