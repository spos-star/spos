import java.util.Scanner;

public class Addition{
    static{
        System.loadLibrary("Addition");
    }
    public native int add(int n1,int n2);
    public static void main(String args[]) {
		Scanner scanner = new Scanner(System.in);
		int num1 = 0, num2 = 0;
		System.out.println("DLL Example Addition Operation");
		System.out.print("Enter first number: ");
		num1 = scanner.nextInt();
		System.out.print("Enter second number: ");
		num2 = scanner.nextInt();
		System.out.println("Result: "+new Addition().add(num1,num2));
		scanner.close();
	}
}