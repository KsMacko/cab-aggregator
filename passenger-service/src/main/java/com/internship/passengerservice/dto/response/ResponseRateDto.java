package com.internship.passengerservice.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ResponseRateDto(
        Integer value,
        Long authorId,
        Long recipientId,
        String rideId,
        Long id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
