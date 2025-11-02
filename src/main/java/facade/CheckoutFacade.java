package facade;

import model.Cart;
import model.Customer;
import model.OrderReceipt;
import model.Product;
import service.NotifierService;
import payment.Payment;
import visitor.ReceiptVisitor;
import visitor.ShippingVisitor;
import visitor.TaxVisitor;

import java.math.BigDecimal;

public class CheckoutFacade {
    private final NotifierService notifier = new NotifierService();

    public OrderReceipt processOrder(Cart cart, Customer customer, Payment paymentPipeline) {
        OrderReceipt receipt = null;

        try {
            double subtotal = cart.getTotal();

            var taxV  = new TaxVisitor();
            var shipV = new ShippingVisitor();
            var lineV = new ReceiptVisitor();

            BigDecimal taxes    = BigDecimal.ZERO;
            BigDecimal shipping = BigDecimal.ZERO;

            StringBuilder receiptLines = new StringBuilder();
            for (Product p : cart.getProducts()) {
                taxes    = taxes.add(p.accept(taxV));
                shipping = shipping.add(p.accept(shipV));
                receiptLines.append(p.accept(lineV))
                        .append(" — ")
                        .append(String.format("₸%.2f", p.getPrice()))
                        .append(System.lineSeparator());
            }

            BigDecimal subtotalBD = BigDecimal.valueOf(subtotal);
            BigDecimal totalDueBD = subtotalBD.add(taxes).add(shipping);
            double totalDue = totalDueBD.doubleValue();

            System.out.printf(
                    "Checkout Items: %d  Subtotal: ₸%.2f  Tax: ₸%.2f  Shipping: ₸%.2f  Total: ₸%.2f%n",
                    cart.getProducts().size(),
                    subtotal,
                    taxes.doubleValue(),
                    shipping.doubleValue(),
                    totalDue
            );

            receipt = paymentPipeline.pay(totalDue);
            notifier.sendReceipt(customer, receipt);

        } catch (IllegalArgumentException e) {
            System.err.println("Error! Invalid data: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error! Something went wrong during checkout: " + e.getMessage());
            e.printStackTrace();
        }

        return receipt;
    }
}
