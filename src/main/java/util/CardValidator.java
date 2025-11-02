package util;

public final class CardValidator {
    private CardValidator() {}

    public static String normalizeDigits(String cardNumber) {
        if (cardNumber == null) return "";
        return cardNumber.replaceAll("\\D", "");
    }

    public static boolean luhnValid(String digits) {
        if (digits == null || digits.length() < 12) return false;
        int sum = 0;
        boolean alt = false;
        for (int i = digits.length() - 1; i >= 0; i--) {
            int n = digits.charAt(i) - '0';
            if (alt) {
                n *= 2;
                if (n > 9) n -= 9;
            }
            sum += n;
            alt = !alt;
        }
        return sum % 10 == 0;
    }

    public static String mask(String digits) {
        if (digits == null || digits.length() < 4) return "****";
        String last4 = digits.substring(digits.length() - 4);
        return "**** **** **** " + last4;
    }
}
