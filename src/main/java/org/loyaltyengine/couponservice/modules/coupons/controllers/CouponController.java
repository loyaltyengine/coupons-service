package org.loyaltyengine.couponservice.modules.coupons.controllers;

import org.loyaltyengine.couponservice.modules.coupons.dtos.CouponDto;
import org.loyaltyengine.couponservice.modules.coupons.dtos.CreateCouponDto;
import org.loyaltyengine.couponservice.modules.coupons.dtos.CouponsResultDto;
import org.loyaltyengine.couponservice.modules.coupons.mappers.CouponMapper;
import org.loyaltyengine.couponservice.modules.coupons.services.CouponService;
import org.loyaltyengine.couponservice.shared.dtos.PaginationQueryDto;
import org.loyaltyengine.couponservice.shared.mappers.SharedMapper;
import org.loyaltyengine.coupons.v1.model.BaseCreateCouponRequest;
import org.loyaltyengine.coupons.v1.model.CouponResponse;
import org.loyaltyengine.coupons.v1.model.CouponsResponse;
import org.loyaltyengine.coupons.v1.model.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "coupons-api/v1")
public class CouponController {
    public static final String PROPERTY_COUPONS_URL = "/properties/{propertyId}/coupons";
    public static final String CUSTOMER_COUPONS_URL = "/properties/{propertyId}/customers/{customerId}/coupons";
    public static final String CUSTOMER_COUPON_URL = "/properties/{propertyId}/customers/{customerId}/coupons/{couponCode}";

    private final CouponService couponService;
    private final CouponMapper couponMapper;
    private final SharedMapper sharedMapper;

    // Create a new coupon
    @PostMapping(value = CUSTOMER_COUPONS_URL)
    public ResponseEntity<CouponResponse> createCoupon(
            // Path parameters
            final @PathVariable String propertyId,
            final @PathVariable String customerId,
            // Request body
            final @RequestBody @Valid BaseCreateCouponRequest createCoupon) {
        // Create dto
        CreateCouponDto createCouponDto = couponMapper.toCreateCouponDto(createCoupon);
        createCouponDto.setPropertyId(propertyId);
        createCouponDto.setCustomerId(customerId);
       
        // Create coupon
        CouponDto couponDto = couponService.createCoupon(createCouponDto);

        // API response
        CouponResponse apiResponse = new CouponResponse()
                .coupon(couponMapper.mapToOpenApiCoupon(couponDto))
                .status(new Status().code(200).message("Coupon created"));

        return ResponseEntity.ok(apiResponse);
    }

    // Get customer coupons
    @GetMapping(value = CUSTOMER_COUPONS_URL)
    public ResponseEntity<CouponsResponse> getCustomerCoupons(
            // Query parameters
            final @RequestParam(required = false, defaultValue = "0") Integer page,
            final @RequestParam(required = false, defaultValue = "100") Integer size,
            final @RequestParam(required = false, defaultValue = "createdAt") String sort,
            final @RequestParam(required = false, defaultValue = "asc") String order,
            // Path parameters
            final @PathVariable String propertyId,
            final @PathVariable String customerId) {

        // Get coupons
        CouponsResultDto result = couponService.getActiveCustomerCoupons(
                propertyId,
                customerId,
                PaginationQueryDto.builder()
                        .page(page)
                        .size(size)
                        .sort(sort)
                        .order(order)
                        .build());

        // API response
        CouponsResponse apiResponse = new CouponsResponse()
                .page(sharedMapper.toClientPage(result.getPage()))
                .coupons(couponMapper.toClientList(result.getCoupons()))
                .status(new Status().code(200).message("Coupons retrieved"));

        return ResponseEntity.ok(apiResponse);
    }

    // Get customer coupon
    @GetMapping(value = CUSTOMER_COUPON_URL)
    public ResponseEntity<CouponResponse> getCustomerCoupon(
            // Path parameters
            final @PathVariable String propertyId,
            final @PathVariable String customerId,
            final @PathVariable String couponCode) {

        // Get coupon
        CouponDto couponDto = couponService.getActiveCustomerCoupon(propertyId, customerId, couponCode);

        // API response
        CouponResponse apiResponse = new CouponResponse()
                .coupon(couponMapper.mapToOpenApiCoupon(couponDto))
                .status(new Status().code(200).message("Coupon retrieved"));

        return ResponseEntity.ok(apiResponse);
    }
}
