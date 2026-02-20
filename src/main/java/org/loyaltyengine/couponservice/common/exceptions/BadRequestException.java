package org.loyaltyengine.couponservice.common.exceptions;

import org.loyaltyengine.openapi.model.ErrorType;

import java.util.List;

import org.loyaltyengine.openapi.model.ErrorDetail;
import lombok.Getter;

@Getter
public class BadRequestException extends ApiException {

    public BadRequestException(ErrorType errorType, String message, String description) {
        super(errorType, message, description);
    }

    public BadRequestException(ErrorType errorType, String message, String description,
            List<ErrorDetail> details) {
        super(errorType, message, description, details);
    }
}
