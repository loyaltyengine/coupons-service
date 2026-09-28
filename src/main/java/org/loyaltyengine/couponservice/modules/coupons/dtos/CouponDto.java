package org.loyaltyengine.couponservice.modules.coupons.dtos;

import java.time.Instant;
import java.util.List;

import org.loyaltyengine.couponservice.shared.models.Amount;
import org.loyaltyengine.couponservice.shared.models.Eligible;
import org.loyaltyengine.couponservice.shared.models.Product;
import org.loyaltyengine.couponservice.shared.models.Usage;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CouponDto {
    private String id;
    private String propertyId;
    private String customerId;
    private String couponCode;
    private String couponType;
    private Usage usage;
    private String description;
    private Boolean isMultiUser;
    private Eligible eligible;
    private Instant expireAt;
    private Instant validFrom;
    private Instant createdAt;
    private Amount amount;
    private Integer percentage;
    private String campaignId;
    private String prefix;
    private Boolean isActive;
    private List<Product> products;
    private List<String> applyToProductsIds;
}
