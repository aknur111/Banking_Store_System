package decorator;

import model.OrderReceipt;
import payment.Payment;

public abstract class PaymentDecorator implements Payment {
    protected final Payment delegate;

    protected PaymentDecorator(Payment delegate) {
        this.delegate = delegate;
    }

    @Override
    public OrderReceipt pay(double amount) {
        return delegate.pay(amount);
    }
}
