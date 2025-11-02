package model;

import visitor.ProductVisitor;

import java.math.BigDecimal;
import java.util.Objects;

public abstract class Product {
    private final String name;
    private final BigDecimal price;

    protected Product(String name, BigDecimal price) {
        this.name = Objects.requireNonNull(name, "name");
        this.price = Objects.requireNonNull(price, "price");
    }

    public String name() {
        return name;
    }
    public BigDecimal price() {
        return price;
    }

    public String getName() {
        return name;
    }
    public double getPrice() {
        return price.doubleValue();
    }

    public abstract <R> R accept(ProductVisitor<R> visitor);
}
