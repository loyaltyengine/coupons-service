package org.loyaltyengine.couponservice.modules.coupons.services;

import java.time.LocalDateTime;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.loyaltyengine.couponservice.common.exceptions.BadRequestException;
import org.loyaltyengine.couponservice.common.exceptions.NotFoundException;
import org.loyaltyengine.couponservice.modules.coupons.dtos.CouponDto;
import org.loyaltyengine.couponservice.modules.coupons.dtos.CreateCouponDto;
import org.loyaltyengine.couponservice.modules.coupons.mappers.CouponMapper;
import org.loyaltyengine.couponservice.modules.coupons.models.Coupon;
import org.loyaltyengine.couponservice.modules.coupons.repositories.CouponRepository;
import org.loyaltyengine.couponservice.shared.utils.CouponCodeGenerator;
import org.loyaltyengine.openapi.model.ErrorType;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponServiceImpl implements CouponService {

    public static final int CREATE_COUPON_MAX_ATTEMPTS = 3;
    public static final int COUPON_CODE_LENGTH = 16;
    public static final String COUPON_CODE_CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String COUPON_CODE_PREFIX = "";

    private final CouponMapper couponMapper;
    private final CouponRepository couponRepository;

    @Override
    public CouponDto createCoupon(CreateCouponDto createCouponDto) {
        Coupon savedCoupon = null;
        int attempt = 1;
        while (attempt <= CREATE_COUPON_MAX_ATTEMPTS) {
            try {
                Coupon newCoupon = couponMapper.toEntity(createCouponDto);
                // Generate a unique coupon code
                String couponCode = CouponCodeGenerator.generate(
                        createCouponDto.getPropertyId() != null ? createCouponDto.getPrefix() : COUPON_CODE_PREFIX,
                        COUPON_CODE_LENGTH,
                        COUPON_CODE_CHARSET);
                newCoupon.setCouponCode(couponCode);
                newCoupon.setCreatedAt(LocalDateTime.now());
                newCoupon.setIsActive(true);
                // Save the new coupon
                savedCoupon = couponRepository.save(newCoupon);
                log.info("Coupon created successfully with code: {} for property: {}", couponCode,
                        newCoupon.getPropertyId());
                break;
            } catch (DuplicateKeyException e) {
                log.warn("Duplicate coupon code for property: {}. Attempt {}/{}",
                        createCouponDto.getPropertyId(), attempt, CREATE_COUPON_MAX_ATTEMPTS);

                if (attempt == CREATE_COUPON_MAX_ATTEMPTS) {
                    log.error("Failed to create coupon after {} attempts due to duplicate key",
                            CREATE_COUPON_MAX_ATTEMPTS);
                    throw e;
                }
                // Continue
                attempt += 1;
            } catch (Exception e) {
                log.error("Error creating coupon on attempt {}/{}: {}", attempt, CREATE_COUPON_MAX_ATTEMPTS,
                        e.getMessage());
                throw e;
            }
        }

        return couponMapper.toDto(savedCoupon);
    }

    @Override
    public CouponDto getValidCustomerCoupon(String propertyId, String customerId, String couponCode) {
        log.info("Getting valid coupon for property: {}, customer: {} and code: {}", propertyId, customerId, couponCode);
        Optional<Coupon> couponOptinal = couponRepository.findByPropertyIdAndCustomerIdAndCouponCode(propertyId,
                customerId, couponCode);

        if (couponOptinal.isEmpty() || Boolean.FALSE.equals(couponOptinal.get().getIsActive())) {
            throw new BadRequestException(ErrorType.INVALID_COUPON,
                    "Invalid coupon code",
                    "No valid coupon of code: " + couponCode + " for property: " + propertyId + " and customer: "
                            + customerId);
        }

        return couponMapper.toDto(couponOptinal.get());
    }

    @Override
    public CouponDto getValidPropertyCoupon(String propertyId, String couponCode) {
        log.info("Getting valid coupon for property: {} and code: {}", propertyId, couponCode);
        Optional<Coupon> couponOptinal = couponRepository.findByPropertyIdAndCouponCode(propertyId, couponCode);

        if (couponOptinal.isEmpty() || Boolean.FALSE.equals(couponOptinal.get().getIsActive())) {
            throw new BadRequestException(ErrorType.INVALID_COUPON,
                    "Invalid coupon code",
                    "No valid coupon of code: " + couponCode + " for property: " + propertyId);
        }

        return couponMapper.toDto(couponOptinal.get());
    }

    @Override
    public void updateCouponUsage(CouponDto couponDto) {
        log.info("Updating coupon for property: {}", couponDto.getPropertyId());
        long updatedCount = couponRepository.findAndSetUsageAndIsActiveById(couponDto.getId(),
                couponDto.getUsage(), couponDto.getIsActive());

        if (updatedCount == 0) {
            throw new NotFoundException(ErrorType.NOT_FOUND, "Coupon not found",
                    "Coupon not found for property: " + couponDto.getPropertyId());
        }
    }

}
