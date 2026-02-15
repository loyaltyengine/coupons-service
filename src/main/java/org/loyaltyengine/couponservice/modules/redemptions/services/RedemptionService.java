package org.loyaltyengine.couponservice.modules.redemptions.services;

import org.loyaltyengine.coupons_service.modules.redemptions.dtos.RedeemCouponDto;
import org.loyaltyengine.coupons_service.modules.redemptions.dtos.RedemptionDto;

public interface RedemptionService {

        public RedemptionDto redeemCoupon(RedeemCouponDto redeemCouponDto);
}
