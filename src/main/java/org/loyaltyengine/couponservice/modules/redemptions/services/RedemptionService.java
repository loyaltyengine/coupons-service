package org.loyaltyengine.couponservice.modules.redemptions.services;

import org.loyaltyengine.couponservice.modules.redemptions.dtos.RedeemCouponDto;
import org.loyaltyengine.couponservice.modules.redemptions.dtos.RedemptionDto;

public interface RedemptionService {

        public RedemptionDto redeemCoupon(RedeemCouponDto redeemCouponDto);
}
