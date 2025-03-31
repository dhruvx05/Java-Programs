import java.util.Scanner; 

public class DivHandling {
    public static void main(String args[]) {
        System.out.println("Dhruv: 500123606");
        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Enter numerator: ");
            int num = sc.nextInt();
            System.out.print("Enter denominator: ");
            int den = sc.nextInt(); // Fixed assignment operator

            int div = num / den; // Division operation
            System.out.println("Division: " + div);
        } 
        catch (ArithmeticException e) { 
            System.out.println("Error: Division by zero is not allowed.");
        } 
        finally {
            System.out.println("Operation completed.");
            sc.close();
        }
    }
}
