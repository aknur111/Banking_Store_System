package app;

import builder.CardBuilder;
import facade.CheckoutFacade;
import model.Cart;
import model.Customer;
import model.Product;
import payment.Payment;
import util.EmailValidator;

import java.util.*;

final class DemoEcommerce {

    private DemoEcommerce() {}

    private record Item(String category, Product product) {}

    static void run() {
        Map<Integer, Item> catalog = buildCatalog();
        Cart cart = new Cart();
        Set<String> usedCategories = new HashSet<>();
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
                System.out.printf("Added: %s | Current total:  %.2f₸%n%n",
                        item.product().toString(), cart.getTotal());
            }

            if (cart.getProducts().isEmpty()) {
                System.out.println("Cart is empty. Bye!");
                return;
            }

            double total = cart.getTotal();
            System.out.printf("%nCart total:  ₸%.2f%n", total);

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

            Customer customer = new Customer(generatedId, name, email);

            String cardNumber;
            Payment payment;
            while (true) {
                System.out.print("Enter your card number: ");
                cardNumber = sc.nextLine().trim();
                try {
                    payment = new CardBuilder()
                            .withCardNumber(cardNumber)
                            .applyBankPolicies(total, effectiveCategory)
                            .build();
                    break;
                } catch (IllegalArgumentException ex) {
                    System.out.println("Card error: " + ex.getMessage());
                }
            }

            CheckoutFacade checkout = new CheckoutFacade();
            checkout.processOrder(cart, customer, payment);
        }
    }

    private static Map<Integer, Item> buildCatalog() {
        Map<Integer, Item> m = new LinkedHashMap<>();
        m.put(1,  new Item("electronics", new Product("e1","Headphones", 15000.0)));
        m.put(2,  new Item("electronics", new Product("e2","Phone",      400000.0)));
        m.put(3,  new Item("electronics", new Product("e3","Laptop",    700000.0)));
        m.put(4,  new Item("electronics", new Product("e4","Tablet",     200000.0)));
        m.put(5,  new Item("food",        new Product("f1","Cake",        2000.0)));
        m.put(6,  new Item("food",        new Product("f2","Pizza",       3200.0)));
        m.put(7,  new Item("food",        new Product("f3","Coffee Beans",3000.0)));
        m.put(8,  new Item("fashion",     new Product("fa1","T-shirt",    10000.0)));
        m.put(9,  new Item("fashion",     new Product("fa2","Jeans",      25000.0)));
        m.put(10, new Item("fashion",     new Product("fa3","Sneakers",  27000.0)));
        m.put(11, new Item("home",        new Product("h1","Lamp",        5600.0)));
        m.put(12, new Item("home",        new Product("h2","Kettle",      4800.0)));
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
}
