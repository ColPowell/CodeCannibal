package org.codecannibal.nmu.acm.main;

/**
 * Created by Colton on 9/23/2015.
 */
public class DecompileTestFile {

	public String one;
	public String two;
	public int three;
	public static int four = 193845;

	public DecompileTestFile(String one, String two, int three){
		this.one = one;
		this.two = two;
		this.three = three;
		this.three = DecompileTestFile.four;
		forLoop();
	}

	public void forLoop(){
		int ab = 0;
		for(int i = 0; i < 10; i++){
			ab += i;
		}
	}

	public void whileLoop(){
		int ab = 0;
		while(ab < 10){
			ab++;
		}
	}

	public void ifStatement(){
		int a = 1;
		int b = 2;
		if(a +2 == b){
			a = b;
		}
		else{
			b = a;
		}
	}


}
