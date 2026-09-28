package org.loyaltyengine.couponservice.shared.enums;

import org.loyaltyengine.couponservice.core.exceptions.BadRequestException;
import org.loyaltyengine.coupons.v1.model.ErrorType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortOrder {
    ASC("asc"),
    DESC("desc");

    private final String value;

    public static SortOrder fromValue(final String value) {
        for (SortOrder sortOrder : SortOrder.values()) {
            if (sortOrder.value.equals(value)) {
                return sortOrder;
            }
        }

        throw new BadRequestException(ErrorType.INVALID_REQUEST, "Invalid sort order",
                "Invalid sort order: " + value + ". Supported values: [asc, desc]");
    }
}
