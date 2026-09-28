package org.loyaltyengine.couponservice.modules.redemptions.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.loyaltyengine.couponservice.core.exceptions.BadRequestException;
import org.loyaltyengine.couponservice.modules.coupons.dtos.CouponDto;
import org.loyaltyengine.couponservice.modules.redemptions.dtos.RedeemCouponDto;
import org.loyaltyengine.couponservice.shared.models.Amount;
import org.loyaltyengine.couponservice.shared.models.Cart;
import org.loyaltyengine.couponservice.shared.models.Eligible;
import org.loyaltyengine.couponservice.shared.models.Product;
import org.loyaltyengine.couponservice.shared.utils.CurrencyUtils;
import org.loyaltyengine.coupons.v1.model.ErrorDetail;
import org.loyaltyengine.coupons.v1.model.ErrorType;
import org.springframework.stereotype.Service;

@Service
public class RedemptionValidationService {

    public void validateRedemption(CouponDto couponDto, RedeemCouponDto redeemCouponDto) {
        // validate multi user
        if (Boolean.FALSE.equals(couponDto.getIsMultiUser())
                && !couponDto.getCustomerId().equals(redeemCouponDto.getCustomerId())) {
            throw new BadRequestException(ErrorType.REDEMPTION_VALIDATION_ERROR, "Invalid customer id",
                    "Customer id " + redeemCouponDto.getCustomerId() + " is not valid for this coupon");
        }

        // Validate cart
        validateCart(couponDto.getEligible(), redeemCouponDto.getCart());
    }

    private void validateCart(Eligible eligible, Cart cart) {
        List<ErrorDetail> errors = new ArrayList<>();

        if (eligible == null) {
            return;
        }

        if (cart == null) {
            addError(errors, "cart", "Cart is required for coupon eligibility validation");
            return;
        }

        validateCategoryIds(eligible.getCategoryIds(), cart.getCategoryIds(), errors);

        validateProductIds(eligible.getProductIds(), cart.getProductIds(), errors);

        validateProductsWithQuantity(eligible.getProducts(), cart.getProducts(), errors);

        validateAmount(eligible.getMinimumAmount(), cart.getAmount(), errors);

        if (!errors.isEmpty()) {
            throw new BadRequestException(ErrorType.REDEMPTION_VALIDATION_ERROR, "Cart is not eligible for this coupon",
                    "Cart validation failed", errors);
        }

    }

    private void validateProductIds(List<String> eligibleProductIds, List<String> cartProductIds,
            List<ErrorDetail> errors) {
        if (eligibleProductIds == null || eligibleProductIds.isEmpty()) {
            return;
        }

        if (cartProductIds == null || cartProductIds.isEmpty()) {
            addError(errors, "cart.productIds", "Cart products are required for coupon eligibility validation");
            return;
        }

        if (!new HashSet<>(cartProductIds).containsAll(eligibleProductIds)) {
            addError(errors, "cart.productIds", "Required product IDs are missing from the cart");
        }
    }

    private void validateCategoryIds(List<String> eligibleCategoryIds, List<String> cartCategoryIds,
            List<ErrorDetail> errors) {
        if (eligibleCategoryIds == null || eligibleCategoryIds.isEmpty()) {
            return;
        }

        if (cartCategoryIds == null || cartCategoryIds.isEmpty()) {
            addError(errors, "cart.categoryIds", "Cart categories are required for coupon eligibility validation");
            return;
        }

        if (!new HashSet<>(cartCategoryIds).containsAll(eligibleCategoryIds)) {
            addError(errors, "cart.categoryIds", "Cart categories are not eligible");
        }
    }

    private void validateProductsWithQuantity(List<Product> eligibleProducts, List<Product> cartProducts,
            List<ErrorDetail> errors) {
        if (eligibleProducts == null || eligibleProducts.isEmpty()) {
            return;
        }

        if (cartProducts == null || cartProducts.isEmpty()) {
            addError(errors, "cart.products", "Cart products are required for coupon eligibility validation");
            return;
        }

        Map<String, Integer> cartMap = cartProducts.stream()
                .collect(Collectors.toMap(
                        Product::getProductId,
                        Product::getQuantity,
                        (qty1, qty2) -> (qty1 == null ? 0 : qty1) + (qty2 == null ? 0 : qty2)));

        if (!eligibleProducts.stream().allMatch(ep -> {
            Integer cartQty = cartMap.get(ep.getProductId());
            return cartQty != null && cartQty >= ep.getQuantity();
        })) {
            addError(errors, "cart.products", "Cart products are not eligible");
        }
    }

    private void validateAmount(Amount minimumAmount, Amount cartAmount, List<ErrorDetail> errors) {
        if (minimumAmount == null || minimumAmount.getValue() == null) {
            return;
        }

        if (cartAmount == null || cartAmount.getValue() == null || cartAmount.getCurrency() == null) {
            addError(errors, "cart.amount", "Cart amount is not valid");
            return;
        }

        if (!CurrencyUtils.isValidISOCurrency(cartAmount.getCurrency())) {
            addError(errors, "cart.amount.currency", "Cart currency is not valid");
            return;
        }

        if (minimumAmount.getCurrency() != null
                && !minimumAmount.getCurrency().equalsIgnoreCase(cartAmount.getCurrency())) {
            addError(errors, "cart.amount.currency", "Cart currency does not match minimum amount currency");
        }

        if (cartAmount.getValue().compareTo(minimumAmount.getValue()) < 0) {
            addError(errors, "cart.amount.value", "Cart amount does not satisfy minimum amount requirement");
        }
    }

    private void addError(List<ErrorDetail> errors, String field, String issue) {
        errors.add(new ErrorDetail().field(field).issue(issue));
    }

}
