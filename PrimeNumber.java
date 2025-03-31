public class PrimeNumbers {
    public static void main(String[] args) {
        System.out.println("Name- Dhruv, SAP- 500123606");
        int count = 0;

        for (int num = 2; num <= 1000; num++) {
            boolean isPrime = true;

            for (int i = 2; i <= Math.sqrt(num); i++) {
                if (num % i == 0) {
                    isPrime = false;
                    break;
                }
            }

            if (isPrime) {
                count++;
            }
        }

        System.out.println("Total number of prime numbers between 1 and 1000: " + count);
    }
}
