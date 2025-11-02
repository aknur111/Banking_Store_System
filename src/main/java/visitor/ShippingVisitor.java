package visitor;

import model.products.*;

import java.math.BigDecimal;

public final class ShippingVisitor implements ProductVisitor<BigDecimal> {

    @Override public BigDecimal visit(Electronics e) {
        return new BigDecimal("1500.00");
    }

    @Override public BigDecimal visit(Food f){
        return new BigDecimal("700.00");
    }

    @Override public BigDecimal visit(Fashion f)  {
        return new BigDecimal("500.00");

    }
    @Override public BigDecimal visit(Home h) {
        return new BigDecimal("1200.00");
    }
}
