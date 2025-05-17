package com.internship.rideservice.dto.transfer;

import com.internship.rideservice.enums.OrderDirection;
import com.internship.rideservice.enums.PromoCodeFieldsToFilter;
import com.internship.rideservice.util.validators.ValidateDate;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static com.internship.rideservice.util.validators.ValidationConstants.DEFAULT_PAGE_NUMBER;
import static com.internship.rideservice.util.validators.ValidationConstants.DEFAULT_PAGE_SIZE;
import static com.internship.rideservice.util.validators.ValidationConstants.MAX_PAGE_SIZE;
import static com.internship.rideservice.util.validators.ValidationConstants.MAX_PAGE_VALUE;
import static com.internship.rideservice.util.validators.ValidationConstants.ORDER_DIRECTION_PATTERN;
import static com.internship.rideservice.util.validators.ValidationConstants.PROMO_CODE_FIELDS_PATTERN;

@Getter
@Setter
@Builder
public class PromoCodeFilterRequest{
    @ValidateDate(message = "date.invalidInput")
    private String createdDate;
    @ValidateDate(message = "date.invalidInput")
    private String validDate;
    @PositiveOrZero(message = "page.positive")
    @Max(value = MAX_PAGE_VALUE, message = "page.max")
    private Integer page=DEFAULT_PAGE_NUMBER;
    @PositiveOrZero(message = "size.positive")
    @Max(value = MAX_PAGE_SIZE, message = "size.max")
    private Integer size=DEFAULT_PAGE_SIZE;
    @Pattern(regexp = PROMO_CODE_FIELDS_PATTERN, message = "field.invalidInput")
    private String sortBy= PromoCodeFieldsToFilter.CREATED_AT.toString();
    @Pattern(regexp = ORDER_DIRECTION_PATTERN, message = "order.direction.invalidInput")
    private String order=OrderDirection.ASC.toString();
}
