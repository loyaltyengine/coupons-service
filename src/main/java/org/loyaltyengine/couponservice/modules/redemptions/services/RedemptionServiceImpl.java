package org.loyaltyengine.couponservice.modules.redemptions.services;


import lombok.RequiredArgsConstructor;
import org.loyaltyengine.coupons_service.modules.coupons.dtos.CouponDto;
import org.loyaltyengine.coupons_service.modules.coupons.services.CouponService;
import org.loyaltyengine.coupons_service.modules.redemptions.dtos.RedeemCouponDto;
import org.loyaltyengine.coupons_service.modules.redemptions.dtos.RedemptionDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedemptionServiceImpl implements RedemptionService {

    private final CouponService couponService;
    private final RedemptionValidationService validator;

    @Override
    public RedemptionDto redeemCoupon(RedeemCouponDto redeemCouponDto) {
        CouponDto coupon = couponService.getCustomerCoupon(
                redeemCouponDto.getPropertyId(),
                redeemCouponDto.getCustomerId(),
                redeemCouponDto.getCouponCode()
        );
        // Validate the redemption request against the coupon details
        validator.validateRedemption(coupon, redeemCouponDto);

        return null;
    }
}
