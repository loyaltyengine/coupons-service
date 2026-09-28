package org.loyaltyengine.couponservice.core.handlers;

import java.util.ArrayList;
import java.util.List;

import org.loyaltyengine.couponservice.core.exceptions.ApiException;
import org.loyaltyengine.couponservice.core.exceptions.BadRequestException;
import org.loyaltyengine.couponservice.core.exceptions.ConflictException;
import org.loyaltyengine.couponservice.core.exceptions.NotFoundException;
import org.loyaltyengine.coupons.v1.model.ErrorDetail;
import org.loyaltyengine.coupons.v1.model.ErrorResponse;
import org.loyaltyengine.coupons.v1.model.ErrorType;
import org.loyaltyengine.coupons.v1.model.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(ApiException e) {
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ApiException e) {
        return buildErrorResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ApiException e) {
        return buildErrorResponse(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        List<ErrorDetail> errors = new ArrayList<>();

        // Add validation errors
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.add(new ErrorDetail().field(fieldName).issue(message));
        });

        ApiException exception = new BadRequestException(
                ErrorType.VALIDATION_ERROR,
                "Bad request",
                "Validation failed", errors);

        return buildErrorResponse(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleApiException(Exception e) {
        ApiException exception = new ApiException(
                ErrorType.INTERNAL_SERVER_ERROR,
                "Internal server error",
                e.getMessage());

        return buildErrorResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(ApiException e, HttpStatus status) {
        //log.error("API Exception caught: {}", e.getMessage(),e);
        log.error("API Exception caught: {}", e.getMessage(),e);
        ErrorResponse errorResponse = new ErrorResponse()
                .status(new Status().code(status.value()).message(e.getMessage()))
                .error(e.getErrorType())
                .description(e.getDescription())
                .details(e.getDetails());

        return new ResponseEntity<>(errorResponse, status);
    }

}
