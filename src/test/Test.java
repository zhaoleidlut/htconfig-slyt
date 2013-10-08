package test;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String num = String.valueOf(11);
		for(int i = 0;i<(6-String.valueOf(11).length());i++) {
			num = "0" + num;
		}
		System.out.println(num);
	}

}
