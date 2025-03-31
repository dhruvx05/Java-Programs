import java.util.Scanner;

public class Scann {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        
        System.out.print("Enter your name: ");
        String name = scan.nextLine();
        
        System.out.print("Enter your age: ");
        int age = scan.nextInt();
        
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        
        scan.close();
    }
}
