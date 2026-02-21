package org.loyaltyengine.couponservice.modules.coupons.tasks;

import org.loyaltyengine.couponservice.modules.coupons.services.CouponService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CouponScheduledTasks {
    private final CouponService couponService;

    @Scheduled(cron = "0 0 0 * * *")
    public void cleanInactiveCoupons() {
        log.info("Cleaning inactive coupons");
        couponService.cleanIanctiveCoupons();
    }

}
