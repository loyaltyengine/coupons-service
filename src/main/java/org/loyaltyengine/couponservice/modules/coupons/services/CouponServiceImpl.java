package org.loyaltyengine.couponservice.modules.coupons.services;

import org.loyaltyengine.coupons_service.common.exceptions.NotFoundException;
import org.loyaltyengine.coupons_service.modules.coupons.dtos.CouponDto;
import org.loyaltyengine.coupons_service.modules.coupons.dtos.CreateCouponDto;
import org.loyaltyengine.coupons_service.modules.coupons.mappers.CouponMapper;
import org.loyaltyengine.coupons_service.modules.coupons.models.Coupon;
import org.loyaltyengine.coupons_service.modules.coupons.repositories.CouponRepository;
import org.loyaltyengine.coupons_service.shared.enums.ErrorType;
import org.loyaltyengine.coupons_service.shared.utils.CouponCodeGenerator;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponServiceImpl implements CouponService {

    public static final int CREATE_COUPON_MAX_ATTEMPTS = 3;
    public static final int COUPON_CODE_LENGHT = 8;
    public static final String COUPON_CODE_CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String COUPON_CODE_PREFIX = "COUPON_";

    private final CouponMapper couponMapper;
    private final CouponRepository couponRepository;

    @Override
    public CouponDto createCoupon(CreateCouponDto createCouponDto) {
        Coupon savedCoupon = null;

        for (int attempt = 1; attempt <= CREATE_COUPON_MAX_ATTEMPTS; attempt++) {
            try {
                Coupon newCoupon = couponMapper.toEntity(createCouponDto);
                // Generate a unique coupon code
                String couponCode = CouponCodeGenerator.generate(COUPON_CODE_PREFIX, COUPON_CODE_LENGHT, COUPON_CODE_CHARSET);
                newCoupon.setCouponCode(couponCode);
                // Save the new coupon
                savedCoupon = couponRepository.save(newCoupon);
                log.info("Coupon created successfully with code: {} for property: {}", couponCode, newCoupon.getPropertyId());
                break;

            } catch (DuplicateKeyException e) {
                log.warn("Duplicate coupon code for property: {}. Attempt {}/{}",
                        createCouponDto.getPropertyId(), attempt, CREATE_COUPON_MAX_ATTEMPTS);

                if (attempt == CREATE_COUPON_MAX_ATTEMPTS) {
                    log.error("Failed to create coupon after {} attempts due to duplicate key", CREATE_COUPON_MAX_ATTEMPTS);
                    throw e;
                }
            } catch (Exception e) {
                log.error("Error creating coupon on attempt {}/{}: {}", attempt, CREATE_COUPON_MAX_ATTEMPTS, e.getMessage());
                throw e;
            }
        }

        return couponMapper.toDto(savedCoupon);
    }

    @Override
    public CouponDto getCustomerCoupon(String propertyId, String customerId, String couponCode) {
        log.info("Getting coupon for property: {}, customer: {} and code: {}", propertyId, customerId, couponCode);
        Coupon coupon = couponRepository.findByPropertyIdAndCustomerIdAndCouponCode(propertyId, customerId, couponCode)
                .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND,"Coupon not found for property: " + propertyId + " and customer: " + customerId));

        return couponMapper.toDto(coupon);
    }

    @Override
    public CouponDto getPropertyCoupon(String propertyId, String couponCode) {
        log.info("Getting coupon for property: {} and code: {}", propertyId, couponCode);
        Coupon coupon = couponRepository.findByPropertyIdAndCouponCode(propertyId, couponCode)
                .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND,"Coupon not found for property: " + propertyId));

        return couponMapper.toDto(coupon);
    }
}
