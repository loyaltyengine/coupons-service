package org.loyaltyengine.couponservice.modules.coupons.repositories;

import java.util.Optional;

import org.loyaltyengine.couponservice.modules.coupons.models.Coupon;
import org.loyaltyengine.couponservice.shared.models.Usage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends MongoRepository<Coupon, String> {

    public boolean existsByPropertyIdAndCouponCode(String couponCode);

    public Optional<Coupon> findByPropertyIdAndCouponCode(String propertyId, String couponCode);

    public Optional<Coupon> findByPropertyIdAndCustomerIdAndCouponCode(String propertyId, String customerId,
            String couponCode);

    @Update("{ '$set' : { 'usage' : ?1, 'isActive' : ?2 } }")
    public long findAndSetUsageAndIsActiveById(String id, Usage usage, Boolean isActive);
}
