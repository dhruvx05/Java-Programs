import java.util.Scanner;

class Area {
    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in);

        System.out.print("Enter the radius of the circle: ");
        double radius = scan.nextDouble();

        double area = Math.PI * radius * radius;
        double circumference = 2 * Math.PI * radius;

        System.out.println("Name - Dhruv, SAP - 500123606");
        System.out.println("Area of the circle: " + area);
        System.out.println("Circumference of the circle: " + circumference);

        scan.close();
    }
}
