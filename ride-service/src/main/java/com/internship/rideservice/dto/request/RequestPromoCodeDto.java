package com.internship.rideservice.dto.request;

import com.internship.rideservice.util.validators.ValidateDate;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import static com.internship.rideservice.util.validators.ValidationConstants.ALPHANUMERIC_REGEX;
import static com.internship.rideservice.util.validators.ValidationConstants.MAX_PROMO_CODE_DISCOUNT;
import static com.internship.rideservice.util.validators.ValidationConstants.MAX_PROMO_CODE_LENGTH;
import static com.internship.rideservice.util.validators.ValidationConstants.MIN_PROMO_CODE_DISCOUNT;

@Builder
public record RequestPromoCodeDto(
        @NotBlank(message = "code.notBlank")
        @Pattern(regexp = ALPHANUMERIC_REGEX, message = "promo.code.invalidInput")
        @Size(max = MAX_PROMO_CODE_LENGTH, message = "promo.code.invalidSize")
        String promoCode,
        @NotNull(message = "discount.notNull")
        @Min(value = MIN_PROMO_CODE_DISCOUNT, message = "discount.min")
        @Max(value = MAX_PROMO_CODE_DISCOUNT, message = "discount.max")
        Byte discount,
        @NotBlank(message = "valid.date.notNull")
        @ValidateDate(message = "date.invalidInput")
        String validUntil
) {
}
