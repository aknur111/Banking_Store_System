package model.products;

import model.Product;
import visitor.ProductVisitor;

import java.math.BigDecimal;

public final class Home extends Product {

    public Home(String name, BigDecimal price) {
        super(name, price);
    }

    public Home(String name, double price) {
        this(name, BigDecimal.valueOf(price));
    }

    @Override
    public <R> R accept(ProductVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
