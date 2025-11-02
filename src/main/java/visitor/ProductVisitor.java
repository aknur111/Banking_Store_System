package visitor;

import model.products.Electronics;
import model.products.Fashion;
import model.products.Food;
import model.products.Home;


public interface ProductVisitor<R> {
    R visit(Electronics e);
    R visit(Food f);
    R visit(Fashion f);
    R visit(Home h);
}
