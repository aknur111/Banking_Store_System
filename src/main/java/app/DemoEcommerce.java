package app;

import builder.CardBuilder;
import facade.CheckoutFacade;
import model.Cart;
import model.Customer;
import model.Product;
import model.products.Electronics;
import model.products.Fashion;
import model.products.Food;
import model.products.Home;
import payment.Payment;
import util.EmailValidator;
import visitor.ShippingVisitor;
import visitor.TaxVisitor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

final class DemoEcommerce {

    private DemoEcommerce() {}

    private record Item(String category, Product product) {}

    static void run() {
        Map<Integer, Item> catalog = buildCatalog();
        Cart cart = new Cart();
        Set<String> usedCategories = new HashSet<>();

        var lineV = new visitor.ReceiptVisitor();

        try (Scanner sc = new Scanner(System.in)) {

            while (true) {
                printCatalog(catalog);
                System.out.print("Choose item number to add (0 = checkout): ");
                int code = readIntSafe(sc);
                if (code == 0) break;

                Item item = catalog.get(code);
                if (item == null) {
                    System.out.println("No such item. Try again.\n");
                    continue;
                }
                cart.addProduct(item.product());
                usedCategories.add(item.category());
                System.out.printf("Added: %s | Current total (subtotal):  ₸%.2f%n%n",
                        item.product().accept(lineV), cart.getTotal());
            }

            if (cart.getProducts().isEmpty()) {
                System.out.println("Cart is empty. Bye!");
                return;
            }

            double subtotal = cart.getTotal();

            var taxV  = new TaxVisitor();
            var shipV = new ShippingVisitor();
//            StringBuilder receiptLines = new StringBuilder();

            BigDecimal taxes    = BigDecimal.ZERO;
            BigDecimal shipping = BigDecimal.ZERO;

            for (Product p : cart.getProducts()) {
                taxes    = taxes.add(p.accept(taxV));
                shipping = shipping.add(p.accept(shipV));
//                receiptLines.append(p.accept(lineV))
//                        .append(System.lineSeparator());
            }

            double totalDue = BigDecimal.valueOf(subtotal)
                    .add(taxes).add(shipping).doubleValue();

            System.out.printf("%nCart subtotal:  ₸%.2f%n", subtotal);
            System.out.printf("Tax:             ₸%.2f%n", taxes.doubleValue());
            System.out.printf("Shipping:        ₸%.2f%n", shipping.doubleValue());
//            System.out.printf("Receipt:%n%s%n", receiptLines);

            System.out.printf("Total to pay:    ₸%.2f%n", totalDue);

            String effectiveCategory = determineEffectiveCategory(usedCategories);
            System.out.println("Effective category for cashback: " + effectiveCategory);

            System.out.print("Enter your full name: ");
            String name = sc.nextLine().trim();
            if (name.isEmpty()) name = "Guest";
            String generatedId = UUID.randomUUID().toString();

            String email;
            while (true) {
                System.out.print("Enter your email (receipt will be sent): ");
                email = sc.nextLine().trim();
                if (EmailValidator.isValid(email)) break;
                System.out.println("Invalid email. Try again (e.g., user@example.com).");
            }

            System.out.println("Enter your initial balance:");
            double initialBalance = readDoubleSafe(sc);
            Customer customer = new Customer(generatedId, name, email, initialBalance);

            while (true) {
                System.out.println("\nWallet:");
                System.out.println("1. View the balance");
                System.out.println("2. Add funds");
                System.out.println("3. Continue payment");
                System.out.print("Choose one: ");
                int choice = (int) readDoubleSafe(sc);
                if (choice == 1) {
                    System.out.printf("Your balance is: ₸%.2f%n", customer.getWallet().getBalance());
                } else if (choice == 2) {
                    System.out.print("Enter amount to add: ");
                    double amount = readDoubleSafe(sc);
                    customer.getWallet().addFunds(amount);
                } else if (choice == 3) {
                    break;
                } else {
                    System.out.println("Unknown option.");
                }
            }

            Payment payment;
            while (true) {
                System.out.print("Enter your card number: ");
                String cardNumber = sc.nextLine().trim();
                try {
                    payment = new CardBuilder()
                            .withCardNumber(cardNumber)
                            .applyBankPolicies(totalDue, effectiveCategory)
                            .build();
                    break;
                } catch (IllegalArgumentException ex) {
                    System.out.println("Card error: " + ex.getMessage());
                }
            }

            if (!customer.getWallet().withdraw(totalDue)) {
                System.out.println("Not enough wallet balance. Please add funds.");
                return;
            }

            CheckoutFacade checkout = new CheckoutFacade();
            checkout.processOrder(cart, customer, payment);
        }
    }

    private static Map<Integer, Item> buildCatalog() {
        Map<Integer, Item> m = new LinkedHashMap<>();
        m.put(1,  new Item("electronics", new Electronics("Headphones", 15000.0, 12)));
        m.put(2,  new Item("electronics", new Electronics("Phone",      400000.0, 24)));
        m.put(3,  new Item("electronics", new Electronics("Laptop",     700000.0, 24)));
        m.put(4,  new Item("electronics", new Electronics("Tablet",     200000.0, 12)));

        m.put(5,  new Item("food",        new Food("Cake",         2000.0,  LocalDate.now().plusDays(3))));
        m.put(6,  new Item("food",        new Food("Pizza",        3200.0,  LocalDate.now().plusDays(1))));
        m.put(7,  new Item("food",        new Food("Coffee Beans", 3000.0,  LocalDate.now().plusDays(30))));

        m.put(8,  new Item("fashion",     new Fashion("T-shirt",   10000.0)));
        m.put(9,  new Item("fashion",     new Fashion("Jeans",     25000.0)));
        m.put(10, new Item("fashion",     new Fashion("Sneakers",  27000.0)));

        m.put(11, new Item("home",        new Home("Lamp",          5600.0)));
        m.put(12, new Item("home",        new Home("Kettle",        4800.0)));
        return m;
    }

    private static void printCatalog(Map<Integer, Item> catalog) {
        System.out.println("\n CATALOG");
        System.out.println("Electronics:");
        printRange(catalog, 1, 4);
        System.out.println("\nFood:");
        printRange(catalog, 5, 7);
        System.out.println("\nFashion:");
        printRange(catalog, 8, 10);
        System.out.println("\nHome:");
        printRange(catalog, 11, 12);
        System.out.println("\n0 - Checkout");
    }

    private static void printRange(Map<Integer, Item> catalog, int from, int to) {
        for (int i = from; i <= to; i++) {
            Item it = catalog.get(i);
            if (it != null) {
                System.out.printf("%d. %s %.2f₸%n", i, it.product().getName(), it.product().getPrice());
            }
        }
    }

    private static int readIntSafe(Scanner sc) {
        while (true) {
            String s = sc.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.print("Enter a number: ");
            }
        }
    }

    private static String determineEffectiveCategory(Set<String> used) {
        if (used.isEmpty()) return "other";
        if (used.size() == 1) return used.iterator().next();
        return "other";
    }

    private static double readDoubleSafe(Scanner sc) {
        while (true) {
            String s = sc.nextLine().trim();
            try{
                return Double.parseDouble(s);
            }catch(NumberFormatException e){
                System.out.print("Enter a number: ");
            }
        }
    }
}
