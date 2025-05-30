package com.internship.rideservice.repo;

import com.internship.rideservice.entity.PromoCode;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PromoCodeRepo extends MongoRepository<PromoCode, String> {
    Optional<PromoCode> findByPromoCodeAndValidUntilAfter(String promoCode, LocalDateTime currentDate);
    Boolean existsByPromoCode(String promoCode);
}
