package visitor;

import model.products.*;

public final class ReceiptVisitor implements ProductVisitor<String> {
    @Override public String visit(Electronics e) {
        return "Electronics: " + e.getName() + " (₸" + String.format("%.2f", e.getPrice()) + ")";
    }

    @Override public String visit(Food f) {
        return "Food: " + f.getName() + " (₸" + String.format("%.2f", f.getPrice()) + ")";
    }

    @Override public String visit(Fashion f) {
        return "Fashion: " + f.getName() + " (₸" + String.format("%.2f", f.getPrice()) + ")";
    }

    @Override public String visit(Home h) {
        return "Home: " + h.getName() + " (₸" + String.format("%.2f", h.getPrice()) + ")";
    }

}
