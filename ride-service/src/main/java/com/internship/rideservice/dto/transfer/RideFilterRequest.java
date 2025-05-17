package com.internship.rideservice.dto.transfer;

import com.internship.rideservice.enums.OrderDirection;
import com.internship.rideservice.enums.RideFieldsToFilter;
import com.internship.rideservice.util.validators.ValidateDate;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static com.internship.rideservice.util.validators.ValidationConstants.DEFAULT_PAGE_NUMBER;
import static com.internship.rideservice.util.validators.ValidationConstants.DEFAULT_PAGE_SIZE;
import static com.internship.rideservice.util.validators.ValidationConstants.FARE_TYPE_PATTERN;
import static com.internship.rideservice.util.validators.ValidationConstants.MAX_ID_VALUE;
import static com.internship.rideservice.util.validators.ValidationConstants.MAX_PAGE_SIZE;
import static com.internship.rideservice.util.validators.ValidationConstants.MAX_PAGE_VALUE;
import static com.internship.rideservice.util.validators.ValidationConstants.ORDER_DIRECTION_PATTERN;
import static com.internship.rideservice.util.validators.ValidationConstants.RIDE_FIELDS_TO_FILTER_PATTERN;
import static com.internship.rideservice.util.validators.ValidationConstants.RIDE_STATUS_PATTERN;

@Getter
@Setter
@Builder
public class RideFilterRequest{
    @ValidateDate(message = "date.invalidInput")
    private String createdDate;
    @PositiveOrZero(message = "id.positive")
    @Max(value = MAX_ID_VALUE, message = "profile.id.maxValue")
    Long driverId;
    @PositiveOrZero(message = "id.positive")
    @Max(value = MAX_ID_VALUE, message = "profile.id.maxValue")
    Long passengerId;
    @Pattern(regexp = RIDE_STATUS_PATTERN, message = "ride.status.invalidInput")
    String status;
    @Pattern(regexp = FARE_TYPE_PATTERN, message = "fare.type.invalidInput")
    String fareType;
    @PositiveOrZero(message = "page.positive")
    @Max(value = MAX_PAGE_VALUE, message = "page.max")
    private Integer page=DEFAULT_PAGE_NUMBER;
    @PositiveOrZero(message = "size.positive")
    @Max(value = MAX_PAGE_SIZE, message = "size.max")
    private Integer size=DEFAULT_PAGE_SIZE;
    @Pattern(regexp = RIDE_FIELDS_TO_FILTER_PATTERN, message = "ride.sortBy.invalidInput")
    String sortBy = RideFieldsToFilter.DATE.toString();
    @Pattern(regexp = ORDER_DIRECTION_PATTERN, message = "order.direction.invalidInput")
    private String order=OrderDirection.ASC.toString();
}