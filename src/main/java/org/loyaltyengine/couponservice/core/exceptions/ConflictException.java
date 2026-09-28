package org.loyaltyengine.couponservice.core.exceptions;

import org.loyaltyengine.coupons.v1.model.ErrorType;

public class ConflictException extends ApiException {

    public ConflictException(ErrorType errorType, String message, String description) {
        super(errorType, message, description);
    }
}
