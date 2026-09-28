package org.loyaltyengine.couponservice.modules.coupons.mappers;

import java.time.LocalDateTime;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

import org.loyaltyengine.couponservice.modules.coupons.dtos.CouponDto;
import org.loyaltyengine.couponservice.modules.coupons.dtos.CreateCouponDto;
import org.loyaltyengine.couponservice.shared.models.Amount;
import org.loyaltyengine.couponservice.shared.models.Eligible;
import org.loyaltyengine.couponservice.shared.models.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import openapitools.CouponsModels.BaseCreateCouponRequest;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CouponGrpcMapper {

    @Mapping(target = "products", source = "productsList")
    CreateCouponDto grpcToDto(BaseCreateCouponRequest request);

    @Mapping(target = "applyToProductsIdsList", ignore = true)
    @Mapping(target = "productsList", ignore = true)
    @Mapping(target = "couponType", source = "couponType")
    @Mapping(target = "eligible", source = "eligible")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "usage", source = "usage")
    openapitools.CouponsModels.Coupon toGrpcCoupon(CouponDto dto);

    @AfterMapping
    default void finalizeCoupon(@MappingTarget openapitools.CouponsModels.Coupon.Builder builder, CouponDto dto) {
        if (dto.getApplyToProductsIds() != null) {
            builder.addAllApplyToProductsIds(dto.getApplyToProductsIds());
        }
        if (dto.getProducts() != null) {
            builder.addAllProducts(dto.getProducts().stream().map(this::toGrpcProduct).toList());
        }
    }

    @Mapping(target = "productIds", source = "productIdsList")
    @Mapping(target = "categoryIds", source = "categoryIdsList")
    @Mapping(target = "products", source = "productsList")
    Eligible grpcToEligible(openapitools.CouponsModels.Eligible eligible);

    @Mapping(target = "productIdsList", ignore = true)
    @Mapping(target = "categoryIdsList", ignore = true)
    @Mapping(target = "productsList", ignore = true)
    openapitools.CouponsModels.Eligible toGrpcEligible(Eligible eligible);

    @AfterMapping
    default void finalizeEligible(@MappingTarget openapitools.CouponsModels.Eligible.Builder builder, Eligible model) {
        if (model.getProductIds() != null)
            builder.addAllProductIds(model.getProductIds());
        if (model.getCategoryIds() != null)
            builder.addAllCategoryIds(model.getCategoryIds());
        if (model.getProducts() != null) {
            builder.addAllProducts(model.getProducts().stream().map(this::toGrpcProduct).toList());
        }
    }

    @Mapping(target = "value", expression = "java(java.math.BigDecimal.valueOf(amount.getValue()))")
    Amount grpcToAmount(openapitools.CouponsModels.Amount amount);

    @Mapping(target = "value", expression = "java(amount.getValue().floatValue())")
    openapitools.CouponsModels.Amount toGrpcAmount(Amount amount);

    Product grpcToProduct(openapitools.CouponsModels.Product product);

    openapitools.CouponsModels.Product toGrpcProduct(Product product);

    @ValueMappings({
            @ValueMapping(source = "COUPON_TYPE_FIXED_AMOUNT", target = "fixed_amount"),
            @ValueMapping(source = "COUPON_TYPE_PERCENTAGE", target = "percentage"),
            @ValueMapping(source = "COUPON_TYPE_FREE_PRODUCT", target = "free_product"),
            @ValueMapping(source = "COUPON_TYPE_FREE_SHIPPING", target = "free_shipping"),
            @ValueMapping(source = "UNRECOGNIZED", target = MappingConstants.NULL)
    })
    String mapGrpcEnumToString(openapitools.CouponsModels.CouponType.Enum grpcEnum);

    @InheritInverseConfiguration(name = "mapGrpcEnumToString")
    @ValueMapping(source = MappingConstants.ANY_REMAINING, target = "COUPON_TYPE_UNSPECIFIED")
    openapitools.CouponsModels.CouponType.Enum mapStringToGrpcEnum(String couponType);

    default LocalDateTime parseDateTime(String value) {
        if (value == null || value.isEmpty())
            return null;
        try {
            return LocalDateTime.parse(value);
        } catch (Exception e) {
            return LocalDateTime.ofInstant(java.time.Instant.parse(value), java.time.ZoneOffset.UTC);
        }
    }

    default String formatDateTime(LocalDateTime value) {
        return value != null ? value.toString() : null;
    }

    default Instant mapStringToInstant(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return Instant.parse(value);
    }

    default String mapInstantToString(Instant value) {
        if (value == null) {
            return null;
        }
        return java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME
                .format(value.atZone(java.time.ZoneOffset.UTC));
    }
}
