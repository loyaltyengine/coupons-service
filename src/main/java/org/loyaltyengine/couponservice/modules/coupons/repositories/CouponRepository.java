package org.loyaltyengine.couponservice.modules.coupons.repositories;

import java.util.Optional;

import org.loyaltyengine.couponservice.modules.coupons.models.Coupon;
import org.loyaltyengine.couponservice.shared.models.Usage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends MongoRepository<Coupon, String> {
    public Optional<Coupon> findByPropertyIdAndCouponCodeAndIsActive(String propertyId, String couponCode, Boolean isActive);

    public Optional<Coupon> findByPropertyIdAndCustomerIdAndCouponCodeAndIsActive(String propertyId, String customerId,
            String couponCode, Boolean isActive);
   
    public Page<Coupon> findByPropertyIdAndCustomerIdAndIsActive(String propertyId, String customerId, Boolean isActive, Pageable pageable);

    @Update("{ '$set' : { 'usage' : ?1, 'isActive' : ?2 } }")
    public long findAndSetUsageAndIsActiveById(String id, Usage usage, Boolean isActive);
}
