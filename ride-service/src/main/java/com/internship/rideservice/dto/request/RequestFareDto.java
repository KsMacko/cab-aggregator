package com.internship.rideservice.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;

import static com.internship.rideservice.util.validators.ValidationConstants.FARE_TYPE_PATTERN;
import static com.internship.rideservice.util.validators.ValidationConstants.MAX_FARE_MIN_PRICE;
import static com.internship.rideservice.util.validators.ValidationConstants.MAX_FREE_WAITING_MINUTES;
import static com.internship.rideservice.util.validators.ValidationConstants.MAX_PAID_WAITING_PRICE;
import static com.internship.rideservice.util.validators.ValidationConstants.MAX_PRICE_PER_KM;
import static com.internship.rideservice.util.validators.ValidationConstants.MAX_PRICE_PER_MINUTE;
import static com.internship.rideservice.util.validators.ValidationConstants.MIN_PRICE;

@Builder
public record RequestFareDto(
        @Pattern(regexp = FARE_TYPE_PATTERN, message = "fare.type.invalidInput")
        String type,
        @NotNull(message = "minPrice.notNull")
        @DecimalMin(value = MIN_PRICE, inclusive = false, message = "minPrice.positive")
        @DecimalMax(value = MAX_FARE_MIN_PRICE, message = "minPrice.maxValue")
        BigDecimal minPrice,
        @NotNull(message = "free.waiting.notNull")
        @Positive(message = "free.waiting.positive")
        @Max(value = MAX_FREE_WAITING_MINUTES, message = "free.waiting.maxValue")
        Integer freeWaiting,
        @NotNull(message = "paid.waiting.notNull")
        @DecimalMin(value = MIN_PRICE, inclusive = false, message = "paid.waiting.positive")
        @DecimalMax(value = MAX_PAID_WAITING_PRICE, message = "paid.waiting.maxValue")
        BigDecimal paidWaitingPrice,
        @NotNull(message = "price.per.km.notNull")
        @DecimalMin(value = MIN_PRICE, inclusive = false, message = "price.per.km.positive")
        @DecimalMax(value = MAX_PRICE_PER_KM, message = "price.per.km.maxValue")
        BigDecimal pricePerKm,
        @NotNull(message = "price.per.min.notNull")
        @DecimalMin(value = MIN_PRICE, inclusive = false, message = "price.per.min.positive")
        @DecimalMax(value = MAX_PRICE_PER_MINUTE, message = "price.per.min.maxValue")
        BigDecimal pricePerMin
){

}
