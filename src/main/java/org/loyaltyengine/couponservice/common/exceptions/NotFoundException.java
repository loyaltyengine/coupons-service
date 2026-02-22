package org.loyaltyengine.couponservice.common.exceptions;

import org.loyaltyengine.openapi.model.ErrorType;

import lombok.Getter;

@Getter
public class NotFoundException extends ApiException {

    public NotFoundException(ErrorType errorType, String message, String description) {
        super(errorType, message, description);
    }
}
