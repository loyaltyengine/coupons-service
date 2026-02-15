package org.loyaltyengine.couponservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages={
        "org.loyaltyengine.coupons_service.modules.coupons.repositories",
        "org.loyaltyengine.coupons_service.modules.redemptions.repositories"
})
public class MongoConfig {

}
