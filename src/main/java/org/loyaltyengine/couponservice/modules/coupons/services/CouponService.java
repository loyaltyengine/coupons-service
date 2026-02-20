package org.loyaltyengine.couponservice.modules.coupons.services;

import org.loyaltyengine.couponservice.modules.coupons.dtos.CouponDto;
import org.loyaltyengine.couponservice.modules.coupons.dtos.CreateCouponDto;

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
    public CouponDto getValidPropertyCoupon(String propertyId, String couponCode);

    /**
     * Get a coupon by propertyId, customerId and couponCode
     * @param propertyId
     * @param customerId
     * @param couponCode
     * @return CouponDto
     */
    public  CouponDto getValidCustomerCoupon(String propertyId, String customerId, String couponCode);

    public void updateCouponUsage(CouponDto couponDto);
}
