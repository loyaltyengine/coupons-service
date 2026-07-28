package org.loyaltyengine.couponservice.modules.redemptions.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.loyaltyengine.couponservice.modules.redemptions.dtos.RedeemCouponDto;
import org.loyaltyengine.couponservice.modules.redemptions.dtos.RedemptionDto;
import org.loyaltyengine.couponservice.modules.redemptions.mappers.RedemptionMapper;
import org.loyaltyengine.couponservice.modules.redemptions.services.RedemptionService;
import org.loyaltyengine.coupons.v1.model.RedemptionRequest;
import org.loyaltyengine.coupons.v1.model.RedemptionResponse;
import org.loyaltyengine.coupons.v1.model.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequestMapping("v1")
@RestController
@RequiredArgsConstructor
public class RedemptionController {
    // API endpoints
    public static final String CUSTOMER_COUPON_REDEMPTIONS_URL = "/properties/{propertyId}/customers/{customerId}/coupons/{couponCode}/redemptions";

    public final RedemptionMapper redemptionMapper;
    public final RedemptionService redemptionService;

    @PostMapping(value = CUSTOMER_COUPON_REDEMPTIONS_URL)
    public ResponseEntity<RedemptionResponse> redeemCoupon(
            @PathVariable String propertyId,
            @PathVariable String customerId,
            @PathVariable String couponCode,
            @RequestBody(required = false) @Valid RedemptionRequest request) {

        // Create dto
        RedeemCouponDto redeemCouponDto = new RedeemCouponDto();
        redeemCouponDto.setCart(request != null ? redemptionMapper.mapCart(request.getCart()) : null);
        redeemCouponDto.setPropertyId(propertyId);
        redeemCouponDto.setCustomerId(customerId);
        redeemCouponDto.setCouponCode(couponCode);

        // Redeem the coupon
        RedemptionDto redemptionDto = redemptionService.redeemCoupon(redeemCouponDto);

        // API response
        RedemptionResponse response = new RedemptionResponse()
                .redemption(redemptionMapper.toClient(redemptionDto))
                .status(new Status().code(200).message("Coupon redeemed"));
        return ResponseEntity.ok(response);
    }

}
