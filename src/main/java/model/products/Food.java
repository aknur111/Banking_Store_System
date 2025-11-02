package model.products;

import model.Product;
import visitor.ProductVisitor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public final class Food extends Product {
    private final LocalDate expiresAt;

    public Food(String name, BigDecimal price, LocalDate expiresAt) {
        super(name, price);
        this.expiresAt = Objects.requireNonNull(expiresAt, "expiresAt");
    }

    public Food(String name, double price, LocalDate expiresAt) {
        this(name, BigDecimal.valueOf(price), expiresAt);
    }

    public LocalDate expiresAt() {
        return expiresAt;
    }

    @Override
    public <R> R accept(ProductVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
