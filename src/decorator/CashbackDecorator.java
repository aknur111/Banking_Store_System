package decorator;

import model.OrderReceipt;
import payment.Payment;

public class CashbackDecorator extends PaymentDecorator {
    private final double percent;

    public CashbackDecorator(Payment delegate, double percent) {
        super(delegate);
        if (percent < 0 || percent > 1) throw new IllegalArgumentException("error, percent must be 0-1");
        this.percent = percent;
    }

    @Override
    public OrderReceipt pay(double amount) {
        double cashback = amount * percent;
        System.out.printf("Cashback +%.0f%% → points: %.2f%n", percent * 100, cashback);
        OrderReceipt base = delegate.pay(amount);
        return new OrderReceipt(base.totalBefore(), base.totalCharged(), base.notes() + " Cashback " + (int)(percent*100) + "%");
    }
}
