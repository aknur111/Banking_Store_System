package service;

import model.OrderReceipt;
import model.Customer;

public class NotifierService {
    public void sendReceipt(Customer customer, OrderReceipt receipt) {
        System.out.printf("Email To: %s  Total: %.2f  Notes: %s%n", customer.getEmail(), receipt.totalCharged(), receipt.notes());
    }
}
