package org.loyaltyengine.couponservice.modules.coupons.models;

import java.time.LocalDateTime;
import java.util.List;

import org.loyaltyengine.coupons_service.shared.models.Amount;
import org.loyaltyengine.coupons_service.shared.models.Eligible;
import org.loyaltyengine.coupons_service.shared.models.Product;
import org.loyaltyengine.coupons_service.shared.models.Usage;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document("coupons")
@CompoundIndexes({
    @CompoundIndex(def = "{'propertyId': 1, 'couponCode': 1}", unique = true)
})
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
    private  Boolean isActive;
    private Boolean isMultiUser; // Indicates if multiple users can use the coupon
    private String couponType; // "Percentage", "Fixed Amount", "Free Product"
    private String description;
    // Dates
    private LocalDateTime validFrom;
    private LocalDateTime expireAt;

    private Eligible eligible;
    private Usage usage;

    // Coupon type specific fields
    private Integer percentage; // For Percentage Type
    private Amount amount; // For Fixed Amount Type
    private List<Product> products; // For Free Product Type
}
