package facade;

import model.Cart;
import model.Customer;
import model.OrderReceipt;
import payment.Payment;
import service.NotifierService;

public class CheckoutFacade {
    private final NotifierService notifier = new NotifierService();

    public OrderReceipt processOrder(Cart cart, Customer customer, Payment paymentPipeline) {
        OrderReceipt receipt = null;

        try {
            double total = cart.getTotal();
            System.out.printf("Checkout Items: %d  Total: %.2f%n", cart.getProducts().size(), total);

            receipt = paymentPipeline.pay(total);
            notifier.sendReceipt(customer, receipt);

        } catch (IllegalArgumentException e) {
            System.err.println("Error! Invalid data: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error!Something went wrong during checkout: " + e.getMessage());
            e.printStackTrace();
        }

        return receipt;
    }
}
