package payment;

import model.OrderReceipt;

public interface Payment {
    OrderReceipt pay(double amount);
}
