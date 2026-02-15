package org.loyaltyengine.couponservice.modules.coupons.repositories;

import java.util.Optional;

import org.loyaltyengine.coupons_service.modules.coupons.models.Coupon;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CouponRepository extends MongoRepository<Coupon, String> {
    
    public  boolean existsByPropertyIdAndCouponCode(String couponCode);
    public  Optional<Coupon> findByPropertyIdAndCouponCode(String propertyId, String couponCode);
    public  Optional<Coupon> findByPropertyIdAndCustomerIdAndCouponCode(String propertyId, String customerId, String couponCode);
}
