package org.loyaltyengine.couponservice.modules.coupons.services;

import org.loyaltyengine.coupons_service.modules.coupons.dtos.CouponDto;
import org.loyaltyengine.coupons_service.modules.coupons.dtos.CreateCouponDto;

public interface CouponService {

    /**
     * Create a new coupon
     * @param createCouponDto
     * @return CouponDto
     */
    public CouponDto createCoupon(CreateCouponDto createCouponDto);

    /**
     * Get a coupon by propertyId and couponCode
     * @param propertyId
     * @param couponCode
     * @return CouponDto
     */
    public CouponDto getPropertyCoupon(String propertyId, String couponCode);

    /**
     * Get a coupon by propertyId, customerId and couponCode
     * @param propertyId
     * @param customerId
     * @param couponCode
     * @return CouponDto
     */
    public  CouponDto getCustomerCoupon(String propertyId, String customerId, String couponCode);
}
