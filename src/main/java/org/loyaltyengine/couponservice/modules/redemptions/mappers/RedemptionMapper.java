package org.loyaltyengine.couponservice.modules.redemptions.mappers;

import org.loyaltyengine.couponservice.modules.redemptions.dtos.RedemptionDto;
import org.loyaltyengine.couponservice.config.SharedMapperConfig;
import org.loyaltyengine.coupons.v1.model.Cart;
import org.loyaltyengine.coupons.v1.model.FixedAmountRedemption;
import org.loyaltyengine.coupons.v1.model.FreeProductRedemption;
import org.loyaltyengine.coupons.v1.model.FreeShippingRedemption;
import org.loyaltyengine.coupons.v1.model.PercentageRedemption;
import org.loyaltyengine.coupons.v1.model.Redemption;
import org.mapstruct.Mapper;

@Mapper(config = SharedMapperConfig.class)
public interface RedemptionMapper {

    RedemptionDto toDto(org.loyaltyengine.couponservice.modules.redemptions.models.Redemption redemption);

    org.loyaltyengine.couponservice.shared.models.Cart mapCart(Cart value);

    default Redemption toClient(RedemptionDto dto) {
        if (dto == null || dto.getCouponType() == null)
            return null;

        return switch (dto.getCouponType().toLowerCase()) {
            case "free_shipping" -> mapToFreeShippingRedemption(dto);
            case "fixed_amount" -> mapToFixedAmountRedemption(dto);
            case "percentage" -> mapToPercentageRedemption(dto);
            case "free_product" -> mapToFreeProductRedemption(dto);
            default -> null;
        };

    }

    FreeShippingRedemption mapToFreeShippingRedemption(RedemptionDto dto);

    FixedAmountRedemption mapToFixedAmountRedemption(RedemptionDto dto);

    PercentageRedemption mapToPercentageRedemption(RedemptionDto dto);

    FreeProductRedemption mapToFreeProductRedemption(RedemptionDto dto);
}
