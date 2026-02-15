package org.loyaltyengine.couponservice.common.exceptions;

import lombok.Getter;
import org.loyaltyengine.coupons_service.shared.enums.ErrorType;

@Getter
public class BadRequestException extends RuntimeException {
    private final ErrorType errorType;

    public BadRequestException( ErrorType errorType, String message) {
        this.errorType = errorType;
        super(message);
    }

}
