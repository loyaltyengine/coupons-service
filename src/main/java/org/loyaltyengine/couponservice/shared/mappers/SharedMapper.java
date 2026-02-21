package org.loyaltyengine.couponservice.shared.mappers;

import java.util.List;

import org.loyaltyengine.couponservice.shared.dtos.PageDto;
import org.loyaltyengine.couponservice.shared.models.Amount;
import org.loyaltyengine.couponservice.shared.models.Product;
import org.loyaltyengine.couponservice.shared.models.Usage;
import org.loyaltyengine.openapi.model.Page;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SharedMapper {

    Product map(org.loyaltyengine.openapi.model.Product source);

    org.loyaltyengine.openapi.model.Product map(Product source);

    Amount map(org.loyaltyengine.openapi.model.Amount source);

    org.loyaltyengine.openapi.model.Amount map(Amount source);

    org.loyaltyengine.openapi.model.CouponUsage map(Usage source);

    List<org.loyaltyengine.openapi.model.Product> mapProducts(List<Product> source);

    Page toClientPage(PageDto source);
}
