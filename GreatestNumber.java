import java.util.Scanner;

public class GreatestNumber {
    public static void main(String[] args) {
	
	System.out.println("Name- Dhruv, SAP- 500123606");
	
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter first number:");
        int num1 = scanner.nextInt();

        System.out.println("Enter second number:");
        int num2 = scanner.nextInt();

        System.out.println("Enter third number:");
        int num3 = scanner.nextInt();

        // Using nested if-else to find the greatest number
        int greatest;
        if (num1 >= num2) {
            if (num1 >= num3) {
                greatest = num1;
            } 
	    else {
                greatest = num3;
            }
        } 
	else {
            if (num2 >= num3) {
                greatest = num2;
            } 
	else {
                greatest = num3;
            }
        }

        System.out.println("The greatest number is: " + greatest);

        scanner.close();
    }
}
