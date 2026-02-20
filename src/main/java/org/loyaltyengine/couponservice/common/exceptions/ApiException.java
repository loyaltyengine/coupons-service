package org.loyaltyengine.couponservice.common.exceptions;

import java.util.List;

import org.loyaltyengine.openapi.model.ErrorDetail;
import org.loyaltyengine.openapi.model.ErrorType;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final ErrorType errorType;
    private final transient List<ErrorDetail> details;
    private final String message;
    private final String description;

    public ApiException(ErrorType errorType, String message, String description) {
        this(errorType, message, description, null);
    }

    public ApiException(ErrorType errorType, String message, String description,
            List<ErrorDetail> details) {
        super(message);
        this.errorType = errorType;
        this.message = message;
        this.description = description;
        this.details = details;
    }

}
