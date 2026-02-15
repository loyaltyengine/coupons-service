package org.loyaltyengine.couponservice.modules.coupons.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CouponDto {
    private String propertyId;
    private String customerId;
    private String couponCode;
    private  Boolean isActive;
    private Boolean isMultiUser;
    private String couponType;
    private String description;
}
