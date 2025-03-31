class NameExc extends Exception {
    public NameExc(String message) {
        super(message);
    }
}

class AgeExc extends Exception {
    public AgeExc(String message) {
        super(message);
    }
}

class Employee {
    private String name;
    private int age;

    // Constructor with validation
    public Employee(String name, int age) throws NameExc, AgeExc {
        validateName(name);
        validateAge(age);
        this.name = name;
        this.age = age;
    }

    private void validateName(String name) throws NameExc {
        if (name.length() < 2) { // Name should have at least 2 characters
            throw new NameExc("Invalid Name: Name should be at least 2 characters long.");
        }
        if (!name.matches("[a-zA-Z ]+")) { // Ensures only letters and spaces
            throw new NameExc("Invalid Name: Name should not contain numbers or special characters.");
        }
    }

    private void validateAge(int age) throws AgeExc {
        if (age > 50) {
            throw new AgeExc("Invalid Age: Age should not exceed 50.");
        }
    }

    public void displayEmployee() {
        System.out.println("Employee Name: " + name);
        System.out.println("Employee Age: " + age);
    }
}

public class EmpValid {
    public static void main(String[] args) {
System.out.println("500123606:Dhruv");
        try {
            Employee emp1 = new Employee("X", 30); 
            emp1.displayEmployee();
        } catch (NameExc | AgeExc e) {
            System.out.println("Exception: " + e.getMessage());
        }

        try {
            Employee emp2 = new Employee("Y", 40); 
            emp2.displayEmployee();
        } catch (NameExc | AgeExc e) {
            System.out.println("Exception: " + e.getMessage());
        }

        try {
            Employee emp3 = new Employee("X", 55); 
            emp3.displayEmployee();
        } catch (NameExc | AgeExc e) {
            System.out.println("Exception: " + e.getMessage());
        }

        try {
            Employee emp4 = new Employee("W", 45); 
            emp4.displayEmployee();
        } catch (NameExc | AgeExc e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}