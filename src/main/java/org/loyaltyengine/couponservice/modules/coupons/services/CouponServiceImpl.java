package org.loyaltyengine.couponservice.modules.coupons.services;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.loyaltyengine.couponservice.common.exceptions.NotFoundException;
import org.loyaltyengine.couponservice.modules.coupons.dtos.CouponDto;
import org.loyaltyengine.couponservice.modules.coupons.dtos.CreateCouponDto;
import org.loyaltyengine.couponservice.modules.coupons.dtos.CouponsResultDto;
import org.loyaltyengine.couponservice.modules.coupons.mappers.CouponMapper;
import org.loyaltyengine.couponservice.modules.coupons.models.Coupon;
import org.loyaltyengine.couponservice.modules.coupons.repositories.CouponRepository;
import org.loyaltyengine.couponservice.shared.constants.SharedConstants;
import org.loyaltyengine.couponservice.shared.dtos.PageDto;
import org.loyaltyengine.couponservice.shared.dtos.PaginationQueryDto;
import org.loyaltyengine.couponservice.shared.enums.CouponSortField;
import org.loyaltyengine.couponservice.shared.enums.SortOrder;
import org.loyaltyengine.couponservice.shared.utils.CouponCodeGenerator;
import org.loyaltyengine.openapi.model.ErrorType;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        log.info("Creating coupon: {}", createCouponDto);
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
    public CouponDto getActiveCustomerCoupon(String propertyId, String customerId, String couponCode) {
        log.info("Getting valid coupon for property: {}, customer: {} and code: {}", propertyId, customerId,
                couponCode);
        Coupon coupon = couponRepository.findActiveCoupon(propertyId, customerId, couponCode)
                .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND, "Coupon not found",
                        "No valid coupon of code: " + couponCode + " for property: " + propertyId));

        return couponMapper.toDto(coupon);
    }

    @Override
    public CouponDto getActivePropertyCoupon(String propertyId, String couponCode) {
        log.info("Getting valid coupon for property: {} and code: {}", propertyId, couponCode);
        Coupon coupon = couponRepository.findActiveCoupon(propertyId, couponCode)
                .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND, "Coupon not found",
                        "No valid coupon of code: " + couponCode + " for property: " + propertyId));

        return couponMapper.toDto(coupon);
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

    @Override
    public CouponsResultDto getActiveCustomerCoupons(String propertyId, String customerId,
            PaginationQueryDto query) {
        log.info("Getting valid coupons for property: {}, customer: {}", propertyId, customerId);
        // Pagination and sorting
        Pageable pageable = buildValidPageable(query);

        // Get coupons
        Page<Coupon> couponsPage = couponRepository.findActiveCoupons(propertyId, customerId, pageable);

        // Map to dto
        return CouponsResultDto.builder()
                .page(PageDto.builder()
                        .page(couponsPage.getNumber())
                        .size(couponsPage.getSize())
                        .totalElements(couponsPage.getTotalElements())
                        .totalPages(couponsPage.getTotalPages())
                        .sort(query.getSort())
                        .order(query.getOrder())
                        .build())
                .coupons(couponMapper.toDtoList(couponsPage.getContent()))
                .build();
    }

    @Override
    public void cleanIanctiveCoupons() {
        log.info("Cleaning inactive coupons");
        couponRepository.deleteInactiveCoupons();
    }

    private Pageable buildValidPageable(PaginationQueryDto dto) {
        int size = dto.getSize() < 0 || dto.getSize() > SharedConstants.MAX_PAGE_SIZE ? SharedConstants.MAX_PAGE_SIZE
                : dto.getSize();
        int page = dto.getPage() < SharedConstants.MIN_PAGE_SIZE ? SharedConstants.MIN_PAGE_SIZE : dto.getPage();

        String sort = CouponSortField.fromValue(dto.getSort()).getValue();
        String order = SortOrder.fromValue(dto.getOrder()).getValue();
        Sort.Direction direction = dto.getOrder().equalsIgnoreCase(order)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return PageRequest.of(
                page,
                size,
                Sort.by(direction, sort));
    }

}
