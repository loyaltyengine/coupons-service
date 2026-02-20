package org.loyaltyengine.couponservice.shared.utils;

import java.util.Currency;

public final class CurrencyUtils {

    private CurrencyUtils() {
    }

    public static boolean isValidISOCurrency(String currency) {
        try {
            Currency.getInstance(currency);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
