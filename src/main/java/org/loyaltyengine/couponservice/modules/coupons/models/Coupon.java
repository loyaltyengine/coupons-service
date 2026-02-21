package org.loyaltyengine.couponservice.modules.coupons.models;

import java.time.LocalDateTime;
import java.util.List;

import org.loyaltyengine.couponservice.shared.models.Amount;
import org.loyaltyengine.couponservice.shared.models.Eligible;
import org.loyaltyengine.couponservice.shared.models.Product;
import org.loyaltyengine.couponservice.shared.models.Usage;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document("coupons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    @Id
    private String id;
    private String propertyId;
    private String customerId;
    private String couponCode;
    private String campaignId;
    private Boolean multiUser;
    private Boolean isActive;
    private Boolean isMultiUser;
    private String couponType;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime validFrom;
    private LocalDateTime expireAt;
    private Eligible eligible;
    private Usage usage;
    // Coupon type specific fields
    private Integer percentage; // percentage
    private Amount amount; // fixed_amount
    private List<String> applyToProductsIds; // fixed_amount and percentage
    private List<Product> products; // free_product
}
