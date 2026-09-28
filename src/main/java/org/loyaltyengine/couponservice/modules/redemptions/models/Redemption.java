package org.loyaltyengine.couponservice.modules.redemptions.models;

import java.time.Instant;
import java.util.List;

import org.loyaltyengine.couponservice.shared.models.Amount;
import org.loyaltyengine.couponservice.shared.models.Product;
import org.loyaltyengine.couponservice.shared.models.Usage;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document("redemptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Redemption {
    @Id
    private String id;
    private String couponCode;
	private String propertyId;
	private String ownedByCustomerId;
	private String redeemedByCustomerId;
	private String couponType;
	private Usage usage;
	private Instant redeemedAt;
	private Amount amount;
	private Integer percentage;
	private List<Product> products;

}
