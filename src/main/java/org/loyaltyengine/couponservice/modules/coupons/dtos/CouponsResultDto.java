package org.loyaltyengine.couponservice.modules.coupons.dtos;

import java.util.List;

import org.loyaltyengine.couponservice.shared.dtos.PageDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CouponsResultDto {
    private PageDto page;
    private List<CouponDto> coupons;
}
