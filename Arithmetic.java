import java.util.Scanner;

class Arithmetic {
    public static void main(String args[]) {
        System.out.println("Name - Dhruv, SAP - 500123606");

        Scanner scan = new Scanner(System.in);

        System.out.print("Enter first number: ");
        double num1 = scan.nextDouble();

        System.out.print("Enter second number: ");
        double num2 = scan.nextDouble();

        System.out.println("Addition: " + (num1 + num2));
        System.out.println("Subtraction: " + (num1 - num2));
        System.out.println("Multiplication: " + (num1 * num2));
        
        if (num2 != 0) {
            System.out.println("Division: " + (num1 / num2));
        } else {
            System.out.println("Error: Division by zero is not allowed.");
        }

        System.out.println("Modulus: " + (num1 % num2));

        scan.close();
    }
}
