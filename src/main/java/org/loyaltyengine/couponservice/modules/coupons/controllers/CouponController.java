package org.loyaltyengine.couponservice.modules.coupons.controllers;

import org.loyaltyengine.couponservice.modules.coupons.dtos.CouponDto;
import org.loyaltyengine.couponservice.modules.coupons.dtos.CreateCouponDto;
import org.loyaltyengine.couponservice.modules.coupons.mappers.CouponMapper;
import org.loyaltyengine.couponservice.modules.coupons.services.CouponService;
import org.loyaltyengine.openapi.model.BaseCreateCouponRequest;
import org.loyaltyengine.openapi.model.CreateCouponResponse;
import org.loyaltyengine.openapi.model.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "v1")
public class CouponController {
    public static final String PROPERTY_COUPONS_URL = "/properties/{propertyId}/coupons";
    public static final String CUSTOMER_COUPONS_URL = "/properties/{propertyId}/customers/{customerId}/coupons";
    public static final String CUSTOMER_COUPON_URL = "/properties/{propertyId}/customers/{customerId}/coupons/{couponCode}";

    private final CouponService couponService;
    private final CouponMapper couponMapper;

    @PostMapping(value = CUSTOMER_COUPONS_URL)
    public ResponseEntity<CreateCouponResponse> createCoupon(
            final @PathVariable String propertyId,
            final @PathVariable String customerId,
            final @RequestBody @Valid BaseCreateCouponRequest createCoupon) {
        // Create dto
        CreateCouponDto createCouponDto = couponMapper.toCreateCouponDto(createCoupon);
        createCouponDto.setPropertyId(propertyId);
        createCouponDto.setCustomerId(customerId);

        // Create coupon
        CouponDto couponDto = couponService.createCoupon(createCouponDto);

        // API response
        CreateCouponResponse apiResponse = new CreateCouponResponse()
                .coupon(couponMapper.mapToOpenApiCoupon(couponDto))
                .status(new Status().code(200).message("Coupon created"));

        return ResponseEntity.ok(apiResponse);
    }
}
