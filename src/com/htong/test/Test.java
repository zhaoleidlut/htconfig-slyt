package com.htong.test;



public class Test {


	static {
		System.loadLibrary("dlltest");
	}

	public native static int sum(int a, int b);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(sum(7, 2));

	}

}
