package org.loyaltyengine.couponservice.config;

import org.loyaltyengine.couponservice.shared.mappers.CommonTypeMapper;
import org.loyaltyengine.couponservice.shared.mappers.SharedModelMapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.MappingConstants;

@MapperConfig(componentModel = MappingConstants.ComponentModel.SPRING, uses = {CommonTypeMapper.class,
        SharedModelMapper.class})
public interface SharedMapperConfig {
}
