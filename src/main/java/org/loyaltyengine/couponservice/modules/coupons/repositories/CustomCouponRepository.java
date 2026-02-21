package org.loyaltyengine.couponservice.modules.coupons.repositories;

import java.util.Optional;

import org.loyaltyengine.couponservice.modules.coupons.models.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomCouponRepository {
    public Optional<Coupon> findActiveCoupon(String propertyId, String couponCode);

    public Optional<Coupon> findActiveCoupon(String propertyId, String customerId, String couponCode);

    public Page<Coupon> findActiveCoupons(String propertyId, String customerId, Pageable pageable);

    public long deleteInactiveCoupons();
}
