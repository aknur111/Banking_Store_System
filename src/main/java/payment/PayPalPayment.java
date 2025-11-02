package payment;

import model.OrderReceipt;

public class PayPalPayment implements Payment {
    private final String account;

    public PayPalPayment(String account) {
        this.account = account;
    }

    @Override
    public OrderReceipt pay(double amount) {
        System.out.printf("PayPal Charging %.2f from %s%n", amount, account);
        return new OrderReceipt(amount, amount, "Paid by PayPal");
    }
}
