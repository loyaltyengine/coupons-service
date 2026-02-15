package org.loyaltyengine.couponservice.shared.utils;

import java.security.SecureRandom;

public final class CouponCodeGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    private CouponCodeGenerator() {
    }

    public static String generate(String prefix, int length, String charset) {
        if (length <= 0) {
            throw new IllegalArgumentException("length must be > 0");
        }

        if (charset == null || charset.isEmpty()) {
            throw new IllegalArgumentException("charset cannot be null or empty");
        }

        StringBuilder sb = new StringBuilder();
        if (prefix != null && !prefix.isEmpty()) {
            sb.append(prefix);
        }

        char[] chars = charset.toCharArray();
        for (int i = 0; i < length; i++) {
            sb.append(chars[RANDOM.nextInt(chars.length)]);
        }
        return sb.toString();
    }
}
