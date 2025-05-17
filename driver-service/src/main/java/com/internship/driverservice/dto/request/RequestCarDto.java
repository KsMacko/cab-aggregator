package com.internship.driverservice.dto.request;

import com.internship.driverservice.utils.validation.ValidationConstants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RequestCarDto (
    @PositiveOrZero(message = "id.positive")
    @Max(value = ValidationConstants.MAX_ID_VALUE, message = "driver.id.maxValue")
    @NotNull(message = "driver.id.notNull")
    Long driverId,
    @NotNull(message = "current.notNull")
    Boolean isCurrent,

    @Pattern(regexp = ValidationConstants.CAR_NUM_PATTERN, message = "car.number.invalidInput")
    @Size(max = ValidationConstants.MAX_CAR_NUMBER_LENGTH, message = "car.number.size")
    @NotBlank(message = "carNumber.notBlank")
    String carNumber,

    @Pattern(regexp = ValidationConstants.LATIN_PATTERN, message = "brand.invalidInput")
    @Size(min = ValidationConstants.MIN_CAR_BRAND_LENGTH, max = ValidationConstants.MAX_CAR_BRAND_LENGTH, message = "car.brand.size")
    @NotBlank(message = "brand.notBlank")
    String brand,

    @Pattern(regexp = ValidationConstants.CYRILLIC_REGEX, message = "color.invalidInput")
    @Size(max = ValidationConstants.MAX_NAME_LENGTH, message = "color.size")
    @NotBlank(message = "color.notBlank")
    String color
){

}
