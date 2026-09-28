package org.loyaltyengine.couponservice.config;

import java.util.stream.Collectors;

import org.loyaltyengine.couponservice.core.exceptions.BadRequestException;
import org.loyaltyengine.couponservice.core.exceptions.ConflictException;
import org.loyaltyengine.couponservice.core.exceptions.NotFoundException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.server.exception.GrpcExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;

import io.grpc.Status;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class GrpcErrorConfig {

    @Bean
    public GrpcExceptionHandler exceptionHandler() {
        return exception -> {
            if (exception instanceof BadRequestException) {
                return Status.INVALID_ARGUMENT
                        .withDescription(exception.getMessage())
                        .asException();
            }

            if (exception instanceof NotFoundException) {

                return Status.NOT_FOUND
                        .withDescription(exception.getMessage())
                        .withCause(exception)
                        .asException();
            }

            if (exception instanceof ConflictException) {
                return Status.ALREADY_EXISTS
                        .withDescription(exception.getMessage())
                        .asException();
            }

            if (exception instanceof MethodArgumentNotValidException e) {
                String details = e.getBindingResult().getFieldErrors().stream()
                        .map(f -> f.getField() + ": " + f.getDefaultMessage())
                        .collect(Collectors.joining(", "));

                return Status.INVALID_ARGUMENT
                        .withDescription("Validation failed: " + details)
                        .asException();
            }

            log.error("Internal server error occurred", exception);
            return Status.INTERNAL
                    .withDescription("Internal server error: " + exception.getMessage())
                    .withCause(exception)
                    .asException();

        };
    }
}
