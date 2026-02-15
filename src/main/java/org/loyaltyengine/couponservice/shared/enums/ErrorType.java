package org.loyaltyengine.couponservice.shared.enums;

import lombok.Getter;

@Getter
public enum ErrorType {
    BAD_REQUEST("bad_request"),
    INTERNAL_SERVER_ERROR("internal_server_error"),
    NOT_FOUND("not_found");

    private final String value;

    ErrorType(String value) {
        this.value = value;
    }
}
