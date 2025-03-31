class NonNumericOperandException extends Exception {
    public NonNumericOperandException(String message) {
        super(message);
    }
}

public class ModCalc {
    public static void main(String args[]) {
	System.out.println("Dhruv:500123606");
        try {
            if (args.length != 2) {
                System.out.println("Usage: java ModCalc <number1> <number2>");
                return;
            }

            double a = parseOperand(args[0]);
            double b = parseOperand(args[1]);

            if (b == 0) {
                System.out.println("Error Found: Cannot calculate Mod with 0 as second number");
                return;
            }

            double m = a % b; // mod operation
            
            System.out.printf("Result: %+f %% %+f = %+f%n", a, b, m);
        }
        catch (NonNumericOperandException e) {
            System.out.println("Error Found: " + e.getMessage());
        }
    }

    private static double parseOperand(String operand) throws NonNumericOperandException {
        try {
            return Double.parseDouble(operand);
        } 
        catch (NumberFormatException e) {
            throw new NonNumericOperandException("Operand '" + operand + "' is not a valid number");
        }
    }
}