package model.products;

import model.Product;
import visitor.ProductVisitor;

import java.math.BigDecimal;

public final class Electronics extends Product {
    private final int warrantyMonths;

    public Electronics(String name, BigDecimal price, int warrantyMonths) {
        super(name, price);
        if (warrantyMonths < 0) throw new IllegalArgumentException("warrantyMonths must be >= 0");
        this.warrantyMonths = warrantyMonths;
    }

    public Electronics(String name, double price, int warrantyMonths) {
        this(name, BigDecimal.valueOf(price), warrantyMonths);
    }

    public int warrantyMonths() {
        return warrantyMonths;
    }

    @Override
    public <R> R accept(ProductVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
