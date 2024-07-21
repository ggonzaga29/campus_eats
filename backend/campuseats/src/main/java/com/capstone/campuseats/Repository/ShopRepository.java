package com.capstone.campuseats.Repository;

import com.capstone.campuseats.Entity.ShopEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopRepository extends MongoRepository<ShopEntity, ObjectId> {
    List<ShopEntity> findByStatus(String status);
    List<ShopEntity> findByStatusNot(String status);
//    List<ShopEntity> findByDeliveryFee(String deliveryFee);
}
