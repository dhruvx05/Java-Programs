import java.util.Scanner;

public class Largestnum {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Name - Dhruv, SAP - 500123606");

        System.out.print("Enter first number: ");
        int a = scanner.nextInt();
        
        System.out.print("Enter second number: ");
        int b = scanner.nextInt();
        
        System.out.print("Enter third number: ");
        int c = scanner.nextInt();
        
        if(a >= b && a >= c)  
            System.out.println(a + " is the largest Number");  
        else if (b >= a && b >= c)  
            System.out.println(b + " is the largest Number");  
        else  
            System.out.println(c + " is the largest number");  

        scanner.close();
    }
}
