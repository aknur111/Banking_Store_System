package util;

import java.util.regex.Pattern;

public final class EmailValidator {
    private EmailValidator() {}

    private static final Pattern EMAIL_RX =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public static boolean isValid(String email) {
        return email != null && EMAIL_RX.matcher(email).matches();
    }
}
