package com.internship.driverservice.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ResponseRateDto(
        Long id,
        Integer value,
        Long authorId,
        Long recipientId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
