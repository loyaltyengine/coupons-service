package org.loyaltyengine.couponservice.modules.redemptions.services;

import org.loyaltyengine.coupons_service.common.exceptions.BadRequestException;
import org.loyaltyengine.coupons_service.modules.coupons.dtos.CouponDto;
import org.loyaltyengine.coupons_service.modules.redemptions.dtos.RedeemCouponDto;
import org.loyaltyengine.coupons_service.shared.enums.ErrorType;
import org.springframework.stereotype.Service;

@Service
public class RedemptionValidationService {

    public void validateRedemption(CouponDto couponDto, RedeemCouponDto redeemCouponDto) {
        // validate multi user
        if(!couponDto.getIsMultiUser() && !couponDto.getCustomerId().equals(redeemCouponDto.getCustomerId())){
            throw  new BadRequestException(ErrorType.BAD_REQUEST, "This coupon is not multi-user");
        }
    }
}
