package com.internship.driverservice.dto.transfer;

import com.internship.driverservice.enums.FieldFilter;
import com.internship.driverservice.enums.OrderDirection;
import com.internship.driverservice.utils.validation.ValidationConstants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.internship.driverservice.utils.validation.ValidationConstants.CAR_NUM_PATTERN;
import static com.internship.driverservice.utils.validation.ValidationConstants.CYRILLIC_REGEX;
import static com.internship.driverservice.utils.validation.ValidationConstants.DEFAULT_PAGE_SIZE;
import static com.internship.driverservice.utils.validation.ValidationConstants.DEFAULT_PAGE_VALUE;
import static com.internship.driverservice.utils.validation.ValidationConstants.DRIVER_STATUS_PATTERN;
import static com.internship.driverservice.utils.validation.ValidationConstants.FARE_TYPE_PATTERN;
import static com.internship.driverservice.utils.validation.ValidationConstants.MAX_PAGE_SIZE;
import static com.internship.driverservice.utils.validation.ValidationConstants.MAX_PAGE_VALUE;
import static com.internship.driverservice.utils.validation.ValidationConstants.MAX_RATE;
import static com.internship.driverservice.utils.validation.ValidationConstants.MIN_RATE;
import static com.internship.driverservice.utils.validation.ValidationConstants.ORDER_DIRECTION;
import static com.internship.driverservice.utils.validation.ValidationConstants.PHONE_PATTERN;
import static com.internship.driverservice.utils.validation.ValidationConstants.PROFILE_SORT_FIELDS;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverFilterRequest {

    @Pattern(regexp = PHONE_PATTERN, message = "phone.invalidInput")
    private String phone;

    @Pattern(regexp = CAR_NUM_PATTERN, message = "car.number.invalidInput")
    private String carNumber;

    @Pattern(regexp = CYRILLIC_REGEX, message = "firstName.invalidInput")
    @Size(max = ValidationConstants.MAX_NAME_LENGTH, message = "firstName.size")
    private String firstName;

    @Pattern(regexp = CYRILLIC_REGEX, message = "lastName.invalidInput")
    @Size(max = ValidationConstants.MAX_NAME_LENGTH, message = "lastName.size")
    private String lastName;

    @Pattern(regexp = DRIVER_STATUS_PATTERN, message = "driver.status.invalidInput")
    private String status;

    @Pattern(regexp = FARE_TYPE_PATTERN, message = "fare.type.invalidInput")
    private String fareType;

    @Min(value = MIN_RATE, message = "rate.minValue")
    @Max(value = MAX_RATE, message = "rate.maxValue")
    private Byte rate;

    @PositiveOrZero(message = "page.positive")
    @Max(value = MAX_PAGE_VALUE, message = "page.max")
    private Integer page = DEFAULT_PAGE_VALUE;

    @PositiveOrZero(message = "size.positive")
    @Max(value = MAX_PAGE_SIZE, message = "size.max")
    private Integer size = DEFAULT_PAGE_SIZE;

    @Pattern(regexp = PROFILE_SORT_FIELDS, message = "sort.by.invalidInput")
    private String sortBy = FieldFilter.FIRST_NAME.toString();

    @Pattern(regexp = ORDER_DIRECTION, message = "order.direction.invalidInput")
    private String order = OrderDirection.ASC.toString();
}