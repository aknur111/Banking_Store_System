package app;

import decorator.CashbackDecorator;
import decorator.DiscountDecorator;
import decorator.FraudDetectionDecorator;
import facade.CheckoutFacade;
import model.Cart;
import model.Customer;
import model.Product;
import payment.CreditCardPayment;
import payment.PayPalPayment;
import payment.Payment;

import java.util.Scanner;

final class DemoEcommerce {

    private DemoEcommerce() {
    }

    static void run() {
        Cart cart = new Cart();
        cart.addProduct(new Product("1", "Headphones", 250.0));
        cart.addProduct(new Product("2", "Mouse", 80.0));
        cart.addProduct(new Product("3", "Keyboard", 130.0));

        Customer customer = new Customer("u1", "Aknur", "aknur_salemxan@mail.ru");

        CheckoutFacade checkout = new CheckoutFacade();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Select payment method:");
        System.out.println("1 - CreditCardPayment with Discount + FraudDetection");
        System.out.println("2 - PayPalPayment with Cashback");
        System.out.println("3 - CreditCardPayment (no decorators)");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();

        Payment paymentPipeline;

        switch (choice) {
            case 1:
                paymentPipeline = new FraudDetectionDecorator(
                        new DiscountDecorator(
                                new CreditCardPayment("1231 1234 5678 9101"), 0.10));
                break;
            case 2:
                paymentPipeline = new CashbackDecorator(
                        new PayPalPayment("aknur_salemxan@paypal"), 0.05);
                break;
            case 3:
                paymentPipeline = new CreditCardPayment("1231 1234 5678 9101");
                break;
            default:
                System.out.println("Invalid choice. Defaulting to option 1.");
                paymentPipeline = new FraudDetectionDecorator(
                        new DiscountDecorator(
                                new CreditCardPayment("1231 1234 5678 9101"), 0.10));
                break;
        }

        try {
            checkout.processOrder(cart, customer, paymentPipeline);
        } catch (Exception e) {
            System.err.println("Error! Checkout failed: " + e.getMessage());
        }
    }
}
