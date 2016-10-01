import java.util.Scanner;

public class Test {
	public static void main(String[] args) {
		Scanner read = new Scanner(System.in);
		System.out.println("Ingrese a: ");
		int a = read.nextInt();
		System.out.println("Ingrese b: ");
		int b = read.nextInt();
		a = a+11;
		b = b-1;
		System.out.println(a);
		System.out.println(b);
	}
}
