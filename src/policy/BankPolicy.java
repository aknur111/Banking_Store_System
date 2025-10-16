package policy;

public final class BankPolicy {
    private BankPolicy() {}
    public static double determineDiscount(double totalAmount) {
        if (totalAmount > 5000) return 0.20;
        if (totalAmount > 2000) return 0.10;
        return 0.0;
    }

    public static double determineCashback(String category) {
        if (category == null) return 0.0;
        switch (category.toLowerCase()) {
            case "electronics": return 0.10;
            case "food":        return 0.05;
            case "fashion":     return 0.08;
            default:            return 0.02;
        }
    }
}
