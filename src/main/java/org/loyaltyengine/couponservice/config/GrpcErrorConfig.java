package org.loyaltyengine.couponservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.server.exception.GrpcExceptionHandler;

import io.grpc.Status;



@Configuration
public class GrpcErrorConfig {

    @Bean
    public GrpcExceptionHandler exceptionHandler() {
        return exception -> {
           if (exception instanceof IllegalArgumentException) {
                return Status.INVALID_ARGUMENT
                        .withDescription(exception.getMessage())
                        .asException();
            }
            return null;
        };
    }
}
