package com.internship.rideservice.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

import static com.internship.rideservice.util.validators.ValidationConstants.ALPHANUMERIC_REGEX;
import static com.internship.rideservice.util.validators.ValidationConstants.FARE_TYPE_PATTERN;
import static com.internship.rideservice.util.validators.ValidationConstants.MAX_ADDRESS_LENGTH;
import static com.internship.rideservice.util.validators.ValidationConstants.MAX_DISTANCE;
import static com.internship.rideservice.util.validators.ValidationConstants.MAX_ID_VALUE;
import static com.internship.rideservice.util.validators.ValidationConstants.MAX_PROMO_CODE_LENGTH;
import static com.internship.rideservice.util.validators.ValidationConstants.MIN_ADDRESS_LENGTH;
import static com.internship.rideservice.util.validators.ValidationConstants.MIN_DISTANCE;
import static com.internship.rideservice.util.validators.ValidationConstants.PAYMENT_TYPE_PATTERN;

@Builder
public record RequestRideDto(
        @NotNull(message = "passenger.notNull")
        @PositiveOrZero(message = "id.positive")
        @Max(value = MAX_ID_VALUE, message = "profile.id.maxValue")
        Long passengerId,
        @Pattern(regexp = ALPHANUMERIC_REGEX, message = "promo.code.invalidInput")
        @Size(max = MAX_PROMO_CODE_LENGTH, message = "promo.code.invalidSize")
        String promoCode,
        @Size(min = MIN_ADDRESS_LENGTH, max = MAX_ADDRESS_LENGTH, message = "start.location.invalidSize")
        String startLocation,
        @NotEmpty(message = "location.list.notNull")
        List<@NotBlank(message = "location.list.elements.notBlank")
        @Size(min = MIN_ADDRESS_LENGTH, max = MAX_ADDRESS_LENGTH, message = "location.list.invalidSize")
                String> endLocation,
        @NotNull(message = "distance.notNull")
        @DecimalMin(value = MIN_DISTANCE, inclusive = false, message = "distance.positive")
        @DecimalMax(value = MAX_DISTANCE, message = "distance.maxValue")
        Float distance,
        @NotBlank(message = "fare.type.notBlank")
        @Pattern(regexp = FARE_TYPE_PATTERN, message = "fare.type.invalidInput")
        String fareType,
        @NotBlank(message = "payment.type.notBlank")
        @Pattern(regexp = PAYMENT_TYPE_PATTERN, message = "payment.type.invalidInput")
        String paymentType
) {
}
