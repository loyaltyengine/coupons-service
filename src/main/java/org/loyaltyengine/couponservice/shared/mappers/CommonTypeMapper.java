package org.loyaltyengine.couponservice.shared.mappers;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;

import org.loyaltyengine.openapi.model.CouponStatus;
import org.loyaltyengine.openapi.model.CouponType;
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

    default LocalDateTime map(OffsetDateTime value) {
        return value == null ? null : value.toLocalDateTime();
    }

    default LocalDateTime map(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return LocalDateTime.parse(value);
        } catch (DateTimeParseException ignored) {
            return OffsetDateTime.parse(value).toLocalDateTime();
        }
    }

    default OffsetDateTime map(LocalDateTime value) {
        return value == null ? null : value.atOffset(ZoneOffset.UTC);
    }
}
