package org.loyaltyengine.couponservice.modules.redemptions.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;

import org.loyaltyengine.couponservice.modules.coupons.dtos.CouponDto;
import org.loyaltyengine.couponservice.modules.coupons.services.CouponService;
import org.loyaltyengine.couponservice.modules.redemptions.dtos.RedeemCouponDto;
import org.loyaltyengine.couponservice.modules.redemptions.dtos.RedemptionDto;
import org.loyaltyengine.couponservice.modules.redemptions.mappers.RedemptionMapper;
import org.loyaltyengine.couponservice.modules.redemptions.models.Redemption;
import org.loyaltyengine.couponservice.modules.redemptions.repositories.RedemptionRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedemptionServiceImpl implements RedemptionService {

    private final CouponService couponService;
    private final RedemptionValidationService validator;
    private final RedemptionRepository repository;
    private final RedemptionMapper redemptionMapper;

    @Override
    public RedemptionDto redeemCoupon(RedeemCouponDto redeemCouponDto) {
        log.info("Redeeming coupon {}", redeemCouponDto.getCouponCode());
        CouponDto coupon = couponService.getActivePropertyCoupon(
                redeemCouponDto.getPropertyId(),
                redeemCouponDto.getCouponCode());

        // Validate the redemption request against the coupon details
        validator.validateRedemption(coupon, redeemCouponDto);

        // Update coupon usage
        if (coupon.getUsage().getBalance() == 1) {
            coupon.getUsage().setBalance(0);
            coupon.setIsActive(false);
        } else {
            coupon.getUsage().setBalance(coupon.getUsage().getBalance() - 1);
        }

        // Update the coupon
        couponService.updateCouponUsage(coupon);

        // Create and save a new redemption
        Redemption redemption = Redemption.builder()
                .couponCode(redeemCouponDto.getCouponCode())
                .propertyId(redeemCouponDto.getPropertyId())
                .ownedByCustomerId(coupon.getCustomerId())
                .redeemedByCustomerId(redeemCouponDto.getCustomerId())
                .amount(coupon.getAmount())
                .percentage(coupon.getPercentage())
                .products(coupon.getProducts())
                .couponType(coupon.getCouponType())
                .usage(coupon.getUsage())
                .redeemedAt(OffsetDateTime.now())
                .build();

        Redemption savedRedemption = repository.save(redemption);

        return redemptionMapper.toDto(savedRedemption);
    }
}
