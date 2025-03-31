import java.util.Scanner;

public class ArrAccess {
    public static void main(String[] args) {
        System.out.println("Dhruv:500123606");
        int[] arr = {1, 2, 3, 4, 5};
        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Enter an index (0-4) of the array: ");
            int index = sc.nextInt();
            
            System.out.println("Element at index " + index + ": " + arr[index]);
        } 
        catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error: Invalid index!!! Please enter a value between 0 and 4.");
        } 
        finally {
            System.out.println("Array access attempted by the user.");
            sc.close(); 
        }
    }
}