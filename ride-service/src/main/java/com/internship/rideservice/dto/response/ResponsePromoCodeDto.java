package com.internship.rideservice.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ResponsePromoCodeDto(
        String promoCode,
        String id,
        Byte discount,
        String validUntil,
        LocalDateTime createdAt
) {
}
