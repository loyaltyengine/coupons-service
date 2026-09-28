package org.loyaltyengine.couponservice.shared.enums;

import org.loyaltyengine.couponservice.core.exceptions.BadRequestException;
import org.loyaltyengine.coupons.v1.model.ErrorType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CouponSortField {
    CREATED_AT("createdAt"),
    EXPIRE_AT("expireAt"),
    VALID_FROM("validFrom");

    private final String value;

    public static CouponSortField fromValue(final String value) {
        for (CouponSortField sortField : CouponSortField.values()) {
            if (sortField.value.equals(value)) {
                return sortField;
            }
        }

        throw new BadRequestException(ErrorType.INVALID_REQUEST, "Invalid sort field",
                "Invalid sort field: " + value + ". Supported values: [createdAt, expireAt, validFrom]");
    }

}
