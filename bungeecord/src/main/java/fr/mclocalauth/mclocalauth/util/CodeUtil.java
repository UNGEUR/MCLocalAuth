package fr.mclocalauth.mclocalauth.util;

import java.security.SecureRandom;

public class CodeUtil {
    private static final String DIGITS = "0123456789";
    private static final SecureRandom RNG = new SecureRandom();

    public static String numericCode(int len) {
        if (len < 4) len = 4;
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            int idx = RNG.nextInt(DIGITS.length());
            sb.append(DIGITS.charAt(idx));
        }
        return sb.toString();
    }
}
