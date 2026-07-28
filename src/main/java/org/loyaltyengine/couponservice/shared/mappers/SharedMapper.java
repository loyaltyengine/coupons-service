package org.loyaltyengine.couponservice.shared.mappers;

import java.util.List;

import org.loyaltyengine.couponservice.shared.dtos.PageDto;
import org.loyaltyengine.couponservice.shared.models.Amount;
import org.loyaltyengine.couponservice.shared.models.Product;
import org.loyaltyengine.couponservice.shared.models.Usage;
import org.loyaltyengine.coupons.v1.model.CouponStatus;
import org.loyaltyengine.coupons.v1.model.CouponType;
import org.loyaltyengine.coupons.v1.model.Page;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SharedMapper {

    Product map(org.loyaltyengine.coupons.v1.model.Product source);

    org.loyaltyengine.coupons.v1.model.Product map(Product source);

    Amount map(org.loyaltyengine.coupons.v1.model.Amount source);

    org.loyaltyengine.coupons.v1.model.Amount map(Amount source);

    org.loyaltyengine.coupons.v1.model.CouponUsage map(Usage source);

    List<org.loyaltyengine.coupons.v1.model.Product> mapProducts(List<Product> source);

    Page toClientPage(PageDto source);
}
