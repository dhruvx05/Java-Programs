import java.util.Scanner;

public class DayOfWeek {
    public static void main(String[] args) {
        System.out.println("Name- Dhruv, SAP- 500123606");
        
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a number (1-7):");
        int day = scanner.nextInt();

        String dayName;
        switch (day) {
            case 1: dayName = "Sunday"; break;
            case 2: dayName = "Monday"; break;
            case 3: dayName = "Tuesday"; break;
            case 4: dayName = "Wednesday"; break;
            case 5: dayName = "Thursday"; break;
            case 6: dayName = "Friday"; break;
            case 7: dayName = "Saturday"; break;
            default: dayName = "Invalid input! Please enter a number between 1 and 7.";
        }

        System.out.println(dayName);
        scanner.close();
    }
}
