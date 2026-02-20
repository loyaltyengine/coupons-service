package org.loyaltyengine.couponservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages={
        "org.loyaltyengine.couponservice.modules.coupons.repositories",
        "org.loyaltyengine.couponservice.modules.redemptions.repositories"
})
public class MongoConfig {

}
