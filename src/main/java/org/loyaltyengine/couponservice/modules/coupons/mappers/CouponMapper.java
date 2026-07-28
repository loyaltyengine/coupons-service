package org.loyaltyengine.couponservice.modules.coupons.mappers;

import java.util.List;

import org.loyaltyengine.couponservice.config.SharedMapperConfig;
import org.loyaltyengine.couponservice.modules.coupons.dtos.CouponDto;
import org.loyaltyengine.couponservice.modules.coupons.dtos.CreateCouponDto;
import org.loyaltyengine.couponservice.modules.coupons.models.Coupon;
import org.loyaltyengine.coupons.v1.model.BaseCreateCouponRequest;
import org.loyaltyengine.coupons.v1.model.CouponResponse;
import org.loyaltyengine.coupons.v1.model.CreateFixedAmountCouponRequest;
import org.loyaltyengine.coupons.v1.model.CreateFreeProductCouponRequest;
import org.loyaltyengine.coupons.v1.model.CreateFreeShippingCouponRequest;
import org.loyaltyengine.coupons.v1.model.CreatePercentageCouponRequest;
import org.loyaltyengine.coupons.v1.model.FixedAmountCoupon;
import org.loyaltyengine.coupons.v1.model.FreeProductCoupon;
import org.loyaltyengine.coupons.v1.model.FreeShippingCoupon;
import org.loyaltyengine.coupons.v1.model.PercentageCoupon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = SharedMapperConfig.class)
public interface CouponMapper {

    CouponDto toDto(Coupon coupon);

    Coupon toEntity(CouponDto couponDto);

    @Mapping(source = "usageLimit", target = "usage.balance")
    @Mapping(source = "usageLimit", target = "usage.limit")
    Coupon toEntity(CreateCouponDto createCouponDto);

    default CreateCouponDto toCreateCouponDto(BaseCreateCouponRequest request) {
        if (request == null)
            return null;

        if (request instanceof CreatePercentageCouponRequest req) {
            return mapPercentage(req);
        } else if (request instanceof CreateFixedAmountCouponRequest req) {
            return mapFixedAmount(req);
        } else if (request instanceof CreateFreeProductCouponRequest req) {
            return mapFreeProduct(req);
        } else if (request instanceof CreateFreeShippingCouponRequest req) {
            return mapFreeShipping(req);
        }

        return null;
    }

    CreateCouponDto mapFreeProduct(CreateFreeProductCouponRequest request);

    CreateCouponDto mapPercentage(CreatePercentageCouponRequest request);

    CreateCouponDto mapFixedAmount(CreateFixedAmountCouponRequest request);

    CreateCouponDto mapFreeShipping(CreateFreeShippingCouponRequest request);

    @Mapping(source = "dto", target = "coupon")
    CouponResponse toCouponResponse(CouponDto dto);

    default org.loyaltyengine.coupons.v1.model.Coupon mapToOpenApiCoupon(CouponDto dto) {
        if (dto == null || dto.getCouponType() == null)
            return null;

        return switch (dto.getCouponType().toLowerCase()) {
            case "percentage" -> toClientPercentage(dto);
            case "fixed_amount" -> toClientFixedAmount(dto);
            case "free_product" -> toClientFreeProduct(dto);
            case "free_shipping" -> toClientFreeShipping(dto);
            default -> toClientFixedAmount(dto);
        };
    }

    FreeProductCoupon toClientFreeProduct(CouponDto dto);

    PercentageCoupon toClientPercentage(CouponDto dto);

    FixedAmountCoupon toClientFixedAmount(CouponDto dto);

    FreeShippingCoupon toClientFreeShipping(CouponDto dto);

    List<CouponDto> toDtoList(List<Coupon> coupons);

    List<org.loyaltyengine.coupons.v1.model.Coupon> toClientList(List<CouponDto> dtos);

}
