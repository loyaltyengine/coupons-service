package org.loyaltyengine.couponservice.common.handlers;

import org.loyaltyengine.couponservice.common.exceptions.ApiException;
import org.loyaltyengine.couponservice.common.exceptions.BadRequestException;
import org.loyaltyengine.couponservice.common.exceptions.NotFoundException;
import org.loyaltyengine.openapi.model.ErrorResponse;
import org.loyaltyengine.openapi.model.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({ BadRequestException.class })
    public ResponseEntity<ErrorResponse> handleBadRequest(ApiException e, WebRequest request) {
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ NotFoundException.class })
    public ResponseEntity<ErrorResponse> handleNotFound(ApiException e, WebRequest request) {
        return buildErrorResponse(e, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(ApiException e, HttpStatus status) {
        log.error("Error occurred: {}", e);
        ErrorResponse errorResponse = new ErrorResponse()
                .status(new Status().code(status.value()).message(e.getMessage()))
                .error(e.getErrorType())
                .description(e.getDescription())
                .details(e.getDetails());

        return new ResponseEntity<>(errorResponse, status);
    }

}
