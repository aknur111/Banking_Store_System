package builder;

import decorator.CashbackDecorator;
import decorator.DiscountDecorator;
import payment.CreditCardPayment;
import payment.Payment;
import policy.BankPolicy;
import util.CardValidator;

public class CardBuilder {
    private String maskedCardNumber;
    private double discount;
    private double cashback;

    public CardBuilder withCardNumber(String userInput) {
        String digits = CardValidator.normalizeDigits(userInput);
        if (!CardValidator.luhnValid(digits)) {
            throw new IllegalArgumentException("Invalid card number. Please try again.");
        }
        this.maskedCardNumber = CardValidator.mask(digits);
        return this;
    }

    public CardBuilder applyBankPolicies(double totalAmount, String category) {
        this.discount = BankPolicy.determineDiscount(totalAmount);
        this.cashback = BankPolicy.determineCashback(category);
        return this;
    }

    public Payment build() {
        if (maskedCardNumber == null || maskedCardNumber.isBlank()) {
            throw new IllegalArgumentException("Card number is required");
        }
        Payment p = new CreditCardPayment(maskedCardNumber);
        if (discount > 0) p = new DiscountDecorator(p, discount);
        if (cashback > 0) p = new CashbackDecorator(p, cashback);
        return p;
    }
}
