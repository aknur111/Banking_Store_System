package decorator;

import model.OrderReceipt;
import payment.Payment;

public class DiscountDecorator extends PaymentDecorator {
    private final double percent;

    public DiscountDecorator(Payment delegate, double percent) {
        super(delegate);
        if (percent < 0 || percent > 1) throw new IllegalArgumentException("percent must be 0-1");
        this.percent = percent;
    }

    @Override
    public OrderReceipt pay(double amount) {
        double discounted = amount * (1 - percent);
        System.out.printf("Discount -%.0f%% - %.2f - %.2f%n", percent * 100, amount, discounted);
        OrderReceipt base = delegate.pay(discounted);
        return new OrderReceipt(base.totalBefore(), discounted, base.notes() + "  Discount " + (int)(percent*100) + "%");
    }
}
