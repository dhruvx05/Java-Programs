class BankAccount {
    private double balance; 

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: " + amount);
        } 
        else {
            System.out.println("Invalid deposit amount.");
        }
    }

    protected void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawn: " + amount);
        } 
        else {
            System.out.println("Invalid withdrawal amount or insufficient balance.");
        }
    }

    void checkBalance() {
        System.out.println("Current Balance: " + balance);
    }
}

class SavingsAccount extends BankAccount {
    public SavingsAccount(double initialBalance) {
        super(initialBalance);
    }

    public void useWithdraw(double amount) {
        withdraw(amount); 
    }
}

public class Bank_acc {
    public static void main(String[] args) {
        BankAccount account = new BankAccount(5000);

        account.deposit(2000);

        account.checkBalance();

        SavingsAccount savings = new SavingsAccount(7000);
        savings.useWithdraw(2000); 
        savings.checkBalance(); 
    }
}
