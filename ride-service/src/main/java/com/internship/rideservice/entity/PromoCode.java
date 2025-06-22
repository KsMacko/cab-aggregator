package com.internship.rideservice.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "promo_codes")
@Getter
@Setter
@FieldNameConstants
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromoCode {
    @Id
    private String id;
    private String promoCode;
    private Byte discount;
    private LocalDateTime createdAt;
    private LocalDateTime validUntil;
}
