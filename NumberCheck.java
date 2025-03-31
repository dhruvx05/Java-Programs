import java.util.Scanner;

public class NumberCheck {
    public static void main(String[] args) {
	
	System.out.println("Name- DHruv, SAP- 500123606");

        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a number:");
        double num = scanner.nextDouble();

        if (num > 0) {
            System.out.println(num + " is a Positive number.");
        } 
	else if (num < 0) {
            System.out.println(num + " is a Negative number.");
        } 
	else {
            System.out.println("The number is a Zero.");
        }

        scanner.close();
    }
}
