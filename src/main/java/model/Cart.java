package model;

import java.util.ArrayList;
import java.util.List;

public class Cart{
    private final List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        products.add(product);
    }

    public double getTotal() {
        double total = 0;
        for (Product p : products) {
            total += p.getPrice();
        }
        return total;
    }

    public List<Product> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Shopping Cart:\n");
        for (Product p : products) {
            sb.append("- ").append(p.toString()).append("\n");
        }
        sb.append(String.format("Total: ₸%.2f", getTotal()));
        return sb.toString();
    }
}
