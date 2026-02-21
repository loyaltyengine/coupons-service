package org.loyaltyengine.couponservice.config;

import org.loyaltyengine.couponservice.shared.mappers.CommonTypeMapper;
import org.loyaltyengine.couponservice.shared.mappers.SharedMapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.MappingConstants;

@MapperConfig(componentModel = MappingConstants.ComponentModel.SPRING, uses = {CommonTypeMapper.class,
        SharedMapper.class})
public interface SharedMapperConfig {
}
