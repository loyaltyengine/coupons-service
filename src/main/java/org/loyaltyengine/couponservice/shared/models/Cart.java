package org.loyaltyengine.couponservice.shared.models;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Cart {
	private List<Product> products;
	private List<String> productIds;
	private List<String> categoryIds;
	private Amount amount;

}
