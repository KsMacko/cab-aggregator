package com.internship.passengerservice.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import static com.internship.passengerservice.utils.ValidationConstants.MAX_ID_VALUE;
import static com.internship.passengerservice.utils.ValidationConstants.MAX_RATE;
import static com.internship.passengerservice.utils.ValidationConstants.MIN_RATE;

@Builder
public record RequestRateDto(
        @Min(value = MIN_RATE, message = "rate.minValue")
        @Max(value = MAX_RATE, message = "rate.maxValue")
        @NotNull(message = "rate.notNull")
        Integer value,
        @Positive(message = "id.positive")
        @Max(value = MAX_ID_VALUE, message = "profile.id.maxValue")
        Long authorId,
        @PositiveOrZero(message = "id.positive")
        @Max(value = MAX_ID_VALUE, message = "profile.id.maxValue")
        @NotNull(message = "profile.notNull")
        Long recipientId,
        @NotBlank(message = "ride.id.notEmpty")
        String rideId
) {
}
