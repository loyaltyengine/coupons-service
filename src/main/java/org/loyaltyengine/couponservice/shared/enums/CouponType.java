package org.loyaltyengine.couponservice.shared.enums;

import org.loyaltyengine.couponservice.core.exceptions.BadRequestException;
import org.loyaltyengine.coupons.v1.model.ErrorType;

import lombok.Getter;

@Getter
public enum CouponType {
    FIXED_AMOUNT("fixed_amount"),
    PERCENTAGE("percentage"),
    FREE_PRODUCT("free_product"),
    FREE_SHIPPING("free_shipping");

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

        throw new BadRequestException(ErrorType.INVALID_REQUEST, "Invalid coupon type", "Invalid coupon type: " + value
                + ". Supported values: [fixed_amount, percentage, free_product, free_shipping]");
    }
}
