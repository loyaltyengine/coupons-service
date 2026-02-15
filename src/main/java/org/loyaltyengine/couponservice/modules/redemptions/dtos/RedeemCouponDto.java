package org.loyaltyengine.couponservice.modules.redemptions.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RedeemCouponDto {
   private String propertyId;
   private String customerId;
   private String couponCode;
}
