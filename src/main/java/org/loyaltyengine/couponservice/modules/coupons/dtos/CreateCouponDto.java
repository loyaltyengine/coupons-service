package org.loyaltyengine.couponservice.modules.coupons.dtos;

import java.time.Instant;
import java.util.List;

import org.loyaltyengine.couponservice.shared.models.Amount;
import org.loyaltyengine.couponservice.shared.models.Eligible;
import org.loyaltyengine.couponservice.shared.models.Product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCouponDto {
    private String propertyId;
    private String campaignId;
    private String customerId;
    private String couponType;
    private String description;
    private Integer usageLimit;
    private Boolean isMultiUser;
    private Eligible eligible;
    private Instant expireAt;
    private Instant validFrom;
    private String prefix;
    private Amount amount;
    private List<Product> products;
    private Integer percentage;
}
