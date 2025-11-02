package payment;

import model.OrderReceipt;

public class CreditCardPayment implements Payment {
    private final String secureCardNumber;

    public CreditCardPayment(String secureCardNumber) {
        this.secureCardNumber = secureCardNumber;
    }


    @Override
    public OrderReceipt pay(double amount) {
        System.out.printf("CreditCard Charging %.2f via %s%n", amount, secureCardNumber);
        return new OrderReceipt(amount, amount, "Paid by Credit Card");
    }
}
