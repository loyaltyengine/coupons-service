package org.loyaltyengine.couponservice.modules.coupons.services;

import org.loyaltyengine.couponservice.modules.coupons.dtos.CouponDto;
import org.loyaltyengine.couponservice.modules.coupons.dtos.CreateCouponDto;
import org.loyaltyengine.couponservice.modules.coupons.dtos.CouponsResultDto;
import org.loyaltyengine.couponservice.shared.dtos.PaginationQueryDto;

public interface CouponService {

    /**
     * Create a new coupon
     * @param createCouponDto coupon dto
     * @return CouponDto
     */
    public CouponDto createCoupon(CreateCouponDto createCouponDto);

    /**
     * Get a coupon by propertyId and couponCode
     * @param propertyId
     * @param couponCode
     * @return CouponDto
     */
    public CouponDto getActivePropertyCoupon(String propertyId, String couponCode);

    /**
     * Get a coupon by propertyId, customerId and couponCode
     * @param propertyId
     * @param customerId
     * @param couponCode
     * @return CouponDto
     */
    public  CouponDto getActiveCustomerCoupon(String propertyId, String customerId, String couponCode);

    /**
     * Update a coupon usage
     * @param couponDto
     */
    public void updateCouponUsage(CouponDto couponDto);

    /**
     * Get paginated customer coupons
     * @param dto
     * @return GetCustomerCouponsResultDto
     */
    public CouponsResultDto getActiveCustomerCoupons(String propertyId, String customerId, PaginationQueryDto query);
}
