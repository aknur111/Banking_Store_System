package visitor;

import model.products.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class TaxVisitor implements ProductVisitor<BigDecimal> {

    @Override public BigDecimal visit(Electronics e) {
        return pct(e.price(), 12);
    }

    @Override public BigDecimal visit(Food f) {
        return BigDecimal.ZERO.setScale(2);
    }

    @Override public BigDecimal visit(Fashion f) {
        return pct(f.price(), 8);
    }

    @Override public BigDecimal visit(Home h){
        return pct(h.price(), 5);
    }

    private static BigDecimal pct(BigDecimal base, int p) {
        return base.multiply(BigDecimal.valueOf(p))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }
}
