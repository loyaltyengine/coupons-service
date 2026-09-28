package org.loyaltyengine.couponservice.shared.mappers;

import java.time.LocalDateTime;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;

import org.loyaltyengine.coupons.v1.model.CouponStatus;
import org.loyaltyengine.coupons.v1.model.CouponType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CommonTypeMapper {

    default String map(CouponType value) {
        return value == null ? null : value.getValue();
    }

    default CouponType mapCouponType(String type) {
        if (type == null) {
            return null;
        }
        return CouponType.fromValue(type);
    }

    default String map(CouponStatus value) {
        return value == null ? null : value.getValue();
    }

    default LocalDateTime map(Instant value) {
        return value == null ? null : java.time.LocalDateTime.ofInstant(value, java.time.ZoneOffset.UTC);
    }

    default LocalDateTime map(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return LocalDateTime.parse(value);
        } catch (DateTimeParseException ignored) {
            return LocalDateTime.ofInstant(java.time.Instant.parse(value), java.time.ZoneOffset.UTC);
        }
    }

    default Instant map(LocalDateTime value) {
        return value == null ? null : value.toInstant(java.time.ZoneOffset.UTC);
    }
}
