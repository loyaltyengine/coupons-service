package org.loyaltyengine.couponservice.core.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.loyaltyengine.coupons.v1.model.ErrorDetail;
import org.loyaltyengine.coupons.v1.model.ErrorType;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final String message;
    private final String description;
    private final ErrorType errorType;
    private final transient List<ErrorDetail> details;

    public ApiException(ErrorType errorType, String message, String description) {
        this(errorType, message, description, new ArrayList<>());
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
