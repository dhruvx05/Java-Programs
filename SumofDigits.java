import java.util.Scanner;

public class SumOfDigits {
    public static void main(String[] args) {
        System.out.println("Name- Dhruv, SAP- 500123606");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter an integer:");
        int num = scanner.nextInt();
        int sum = 0;

        num = Math.abs(num); // To handle negative numbers

        while (num > 0) {
            sum += num % 10;
            num /= 10;
        }

        System.out.println("Sum of digits: " + sum);
        scanner.close();
    }
}
