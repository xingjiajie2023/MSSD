package net;

public class Tcp_Package {

	static{
		System.loadLibrary("tcp_package");
	}
	
/*	public static void main(String[] args) {
		String s1="root A";
		String s2="A B"+"\n"+"A C";
		Tcp_Package tp = new Tcp_Package();
		int flag = -1;
		flag = tp.new tcp_package().contain(s1, s2);
		System.out.println("flag = "+flag);
	}*/
	
	public static int contains2(String s1, String s2, int index){
		if(s1==null){
			System.out.println("s1 is null");
			return 3;
		}
		if(s2==null){
			System.out.println("s2 is null");
			return -1;
		}
		Tcp_Package tp = new Tcp_Package();
		int flag = -1;
		flag = tp.new tcp_package().contain(s1, s2);
		//System.out.println("flag = "+flag);
		return flag;
	}
	
	class tcp_package{
		public native int contain(String s1, String s2);
	}
}
