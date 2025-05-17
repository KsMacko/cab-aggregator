package com.internship.driverservice.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import static com.internship.driverservice.utils.validation.ValidationConstants.MAX_ID_VALUE;
import static com.internship.driverservice.utils.validation.ValidationConstants.MAX_RATE;
import static com.internship.driverservice.utils.validation.ValidationConstants.MIN_RATE;

@Builder
public record RequestRateDto(
        @Min(value = MIN_RATE, message = "rate.minValue")
        @Max(value = MAX_RATE, message = "rate.maxValue")
        @NotNull(message = "value.notNull")
        Integer value,
        @PositiveOrZero(message = "id.positive")
        @Max(value = MAX_ID_VALUE, message = "authorId.id.maxValue")
        @NotNull(message = "authorId.notNull")
        Long authorId,
        @PositiveOrZero(message = "id.positive")
        @Max(value = MAX_ID_VALUE, message = "profile.id.maxValue")
        @NotNull(message = "profile.notNull")
        Long recipientId,
        @NotBlank(message = "ride.id.notEmpty")
        String rideId
) {
}
