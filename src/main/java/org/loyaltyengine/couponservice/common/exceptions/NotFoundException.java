package org.loyaltyengine.couponservice.common.exceptions;

import lombok.Getter;
import org.loyaltyengine.coupons_service.shared.enums.ErrorType;

@Getter
public class NotFoundException extends RuntimeException {
    private final ErrorType errorType;
    public NotFoundException(ErrorType errorType, String message) {
        this.errorType = errorType;
        super(message);
    }

}
