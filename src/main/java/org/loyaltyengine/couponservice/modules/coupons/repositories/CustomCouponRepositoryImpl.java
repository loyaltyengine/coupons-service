package org.loyaltyengine.couponservice.modules.coupons.repositories;

import java.time.OffsetDateTime;
import java.util.Optional;

import org.loyaltyengine.couponservice.modules.coupons.models.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class CustomCouponRepositoryImpl implements CustomCouponRepository {

    private static final String FIELD_PROPERTY_ID = "propertyId";
    private static final String FIELD_COUPON_CODE = "couponCode";
    private static final String FIELD_IS_ACTIVE = "isActive";
    private static final String FIELD_EXPIRE_AT = "expireAt";
    private static final String FIELD_CUSTOMER_ID = "customerId";

    private final MongoTemplate mongoTemplate;

    @Override
    public Optional<Coupon> findActiveCoupon(String propertyId, String couponCode) {
        Query query = new Query(Criteria.where(FIELD_PROPERTY_ID).is(propertyId)
            .and(FIELD_COUPON_CODE).is(couponCode)
            .and(FIELD_IS_ACTIVE).is(true)
            .and(FIELD_EXPIRE_AT).gt(OffsetDateTime.now()));
        return Optional.ofNullable(mongoTemplate.findOne(query, Coupon.class));
    }

    @Override
    public Optional<Coupon> findActiveCoupon(String propertyId, String customerId, String couponCode) {
        Query query = new Query(Criteria.where(FIELD_PROPERTY_ID).is(propertyId)
            .and(FIELD_COUPON_CODE).is(couponCode)
            .and(FIELD_IS_ACTIVE).is(true)
            .and(FIELD_EXPIRE_AT).gt(OffsetDateTime.now()));
        return Optional.ofNullable(mongoTemplate.findOne(query, Coupon.class));

    }

    @Override
    public Page<Coupon> findActiveCoupons(String propertyId, String customerId, Pageable pageable) {
        Query query = new Query(Criteria.where(FIELD_PROPERTY_ID).is(propertyId)
            .and(FIELD_CUSTOMER_ID).is(customerId)
            .and(FIELD_IS_ACTIVE).is(true)
            .and(FIELD_EXPIRE_AT).gt(OffsetDateTime.now()))
                .with(pageable);

        return PageableExecutionUtils.getPage(
                mongoTemplate.find(query, Coupon.class),
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Coupon.class));
    }

    @Override
    public long deleteInactiveCoupons() {
        Query query = new Query(Criteria.where(FIELD_IS_ACTIVE).is(false)
            .and(FIELD_EXPIRE_AT).lt(OffsetDateTime.now()));
        return mongoTemplate.remove(query, Coupon.class).getDeletedCount();
    }

}
