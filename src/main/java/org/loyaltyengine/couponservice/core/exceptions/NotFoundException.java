package org.loyaltyengine.couponservice.core.exceptions;

import org.loyaltyengine.coupons.v1.model.ErrorType;

import lombok.Getter;

@Getter
public class NotFoundException extends ApiException {

    public NotFoundException(ErrorType errorType, String message, String description) {
        super(errorType, message, description);
    }
}
