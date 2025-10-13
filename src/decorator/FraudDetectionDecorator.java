package decorator;

import model.OrderReceipt;
import payment.Payment;

public class FraudDetectionDecorator extends PaymentDecorator {
    public FraudDetectionDecorator(Payment delegate) {
        super(delegate);
    }

    @Override
    public OrderReceipt pay(double amount) {
        if (amount > 1500.0) {
            System.out.printf("Fraud Suspicious amount: %.2f — logging & risk scoring...%n", amount);
        }
        OrderReceipt base = delegate.pay(amount);
        return new OrderReceipt(base.totalBefore(), base.totalCharged(), base.notes() + "  FraudChecked");
    }
}
