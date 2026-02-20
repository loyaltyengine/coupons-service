package org.loyaltyengine.couponservice.modules.redemptions.mappers;

import org.loyaltyengine.couponservice.modules.redemptions.dtos.RedemptionDto;
import org.loyaltyengine.couponservice.config.SharedMapperConfig;
import org.loyaltyengine.openapi.model.Cart;
import org.loyaltyengine.openapi.model.FixedAmountRedemption;
import org.loyaltyengine.openapi.model.FreeProductRedemption;
import org.loyaltyengine.openapi.model.FreeShippingRedemption;
import org.loyaltyengine.openapi.model.PercentageRedemption;
import org.loyaltyengine.openapi.model.Redemption;
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
