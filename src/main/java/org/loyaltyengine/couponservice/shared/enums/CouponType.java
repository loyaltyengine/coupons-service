package org.loyaltyengine.couponservice.shared.enums;

import lombok.Getter;

@Getter
public enum CouponType {
    FIXED_AMOUNT("fixed_amount"),
    PERCENTAGE("percentage"),
    FREE_PRODUCT("free_product"),
    FREE_SHIPPING("free_shipping"),
    INVALID_TYPE("invalid_type");

    private final String value;

    CouponType(final String value) {
        this.value = value;
    }

    public static CouponType fromValue(final String value) {
        for (CouponType couponType : CouponType.values()) {
            if (couponType.value.equals(value)) {
                return couponType;
            }
        }
        return INVALID_TYPE;
    }
}
