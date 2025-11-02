package model;

public class Wallet {
    private double balance;

    public Wallet(double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        this.balance = initialBalance;
    }

    public double getBalance() {
        return balance;
    }

    public void addFunds(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        balance += amount;
        System.out.printf("Successfully added ₸%.2f. New balance: ₸%.2f%n", amount, balance);
    }

    public boolean withdraw(double amount) {
        if (amount > balance) {
            System.out.println(" Not enough balance. Please add funds.");
            return false;
        }
        balance -= amount;
        System.out.printf("₸%.2f withdrawn. Remaining balance: ₸%.2f%n", amount, balance);
        return true;
    }
}
