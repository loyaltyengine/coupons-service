package org.loyaltyengine.couponservice.modules.coupons.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCouponDto {
    private String propertyId;
    private String customerId;
    private String couponCode;
}
