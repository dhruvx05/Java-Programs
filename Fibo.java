import java.util.Scanner;

public class Fibo {
    public static void main(String[] args) {
        System.out.println("Name- Dhruv, SAP- 500123606");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the number of terms for Fibonacci series:");
        int n = scanner.nextInt();

        int a = 0, b = 1;
        
        System.out.println("Fibonacci Series:");

        for (int i = 1; i <= n; i++) {
            System.out.print(a + " ");
            int nextTerm = a + b;
            a = b;
            b = nextTerm;
        }

        scanner.close();
    }
}
