package model;

public class Customer {
    private final String id;
    private final String name;
    private final String email;
    private final Wallet wallet;

    public Customer(String id, String name, String email, double initialBalance) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.wallet = new Wallet(initialBalance);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
    public Wallet getWallet() {
        return wallet;
    }

    @Override
    public String toString() {
        return String.format("Customer[id=%s, name=%s, email=%s, balance=₸%.2f]",
                id, name, email, wallet.getBalance());
    }
}
