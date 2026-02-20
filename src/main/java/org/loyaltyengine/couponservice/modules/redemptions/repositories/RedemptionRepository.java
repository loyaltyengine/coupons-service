package org.loyaltyengine.couponservice.modules.redemptions.repositories;

import org.loyaltyengine.couponservice.modules.redemptions.models.Redemption;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedemptionRepository extends MongoRepository<Redemption, String> {

}
