package org.loyaltyengine.couponservice.modules.redemptions.dtos;

import java.time.OffsetDateTime;
import java.util.List;

import org.loyaltyengine.couponservice.shared.models.Amount;
import org.loyaltyengine.couponservice.shared.models.Product;
import org.loyaltyengine.couponservice.shared.models.Usage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RedemptionDto {
	private String couponCode;
	private String propertyId;
	private String ownedByCustomerId;
	private String redeemedByCustomerId;
	private String couponType;
	private Usage usage;
	private OffsetDateTime redeemedAt;
	private Amount amount;
	private Integer percentage;
	private List<Product> products;
}
