import java.util.Scanner;

class Bank {
    int acc_no, balance, dep_ammount, wd_ammount;
    String name;

    public void user_input() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the Account Number:");
        acc_no = sc.nextInt();
        sc.nextLine();

        System.out.println("Enter Name:");
        name = sc.nextLine();

        System.out.println("Enter Initial Balance:");
        balance = sc.nextInt();

        System.out.println("\nAccount Details:");
        System.out.println("Account Number: " + acc_no);
        System.out.println("Account Holder: " + name);
        System.out.println("Balance: " + balance);
    }

    public void deposit(Scanner sc) {
        System.out.println("\nEnter the amount to be deposited:");
        dep_ammount = sc.nextInt();
        balance += dep_ammount;
        System.out.println("Deposit Successful! Updated Balance: " + balance);
    }

    public void withdraw(Scanner sc) {
        System.out.println("\nEnter the amount to be withdrawn:");
        wd_ammount = sc.nextInt();

        if (wd_ammount > balance) {
            System.out.println("Insufficient Balance!");
        } else {
            balance -= wd_ammount;
            System.out.println("Withdrawal Successful! Updated Balance: " + balance);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Bank bank = new Bank();
        bank.user_input();

        while (true) {
            System.out.println("\nChoose one of the following options:");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    bank.deposit(sc);
                    break;
                case 2:
                    bank.withdraw(sc);
                    break;
                case 3:
                    System.out.println("Thank you!");
                    sc.close();
                    return; 
                default:
                    System.out.println("Invalid choice! Please try again .");
            }
        }
    }
}
