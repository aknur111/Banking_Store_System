package java.visitor;

import model.products.Electronics;
import model.products.Fashion;
import model.products.Food;
import model.products.Home;

import org.junit.jupiter.api.Test;
import visitor.TaxVisitor;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaxVisitorTest {

    @Test
    void calculatesTaxPerCategory() {
        var taxV = new TaxVisitor();

        var e = new Electronics("Phone", new BigDecimal("400000.00"), 24);
        var f = new Food("Cake", new BigDecimal("2000.00"), LocalDate.now().plusDays(2));
        var fa = new Fashion("T-shirt", new BigDecimal("10000.00"));
        var h = new Home("Lamp", new BigDecimal("5600.00"));

        assertEquals(new BigDecimal("48000.00"), e.accept(taxV));
        assertEquals(new BigDecimal("0.00"), f.accept(taxV));
        assertEquals(new BigDecimal("800.00"), fa.accept(taxV));
        assertEquals(new BigDecimal("280.00"), h.accept(taxV));
    }
}
