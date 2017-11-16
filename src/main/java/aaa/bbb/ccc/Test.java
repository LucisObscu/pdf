package aaa.bbb.ccc;

import java.io.File;

public class Test {
	public static void main(String ar[]) {
		String a="1k2k3k4k5k6k7k8k9k";
		int b=a.lastIndexOf("k");
		String c=a.substring(0,b);
		System.out.println(c);
	}
}
