package com.internship.passengerservice.dto.transfer;

import com.internship.passengerservice.enums.FieldsToSort;
import com.internship.passengerservice.enums.OrderDirection;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static com.internship.passengerservice.utils.ValidationConstants.DEFAULT_PAGE_SIZE;
import static com.internship.passengerservice.utils.ValidationConstants.DEFAULT_PAGE_VALUE;
import static com.internship.passengerservice.utils.ValidationConstants.EMAIL_PATTERN;
import static com.internship.passengerservice.utils.ValidationConstants.MAX_EMAIL_LENGTH;
import static com.internship.passengerservice.utils.ValidationConstants.MAX_PAGE_SIZE;
import static com.internship.passengerservice.utils.ValidationConstants.MAX_PAGE_VALUE;
import static com.internship.passengerservice.utils.ValidationConstants.MAX_RATE;
import static com.internship.passengerservice.utils.ValidationConstants.MIN_RATE;
import static com.internship.passengerservice.utils.ValidationConstants.ORDER_DIRECTION;
import static com.internship.passengerservice.utils.ValidationConstants.PHONE_PATTERN;
import static com.internship.passengerservice.utils.ValidationConstants.PROFILE_SORT_FIELDS;

@Getter
@Setter
@Builder
public class ProfileFilterRequest{
    @Pattern(regexp = EMAIL_PATTERN, message = "email.invalidInput")
    @Size(max = MAX_EMAIL_LENGTH, message = "email.invalidSize")
    private String email;
    @Pattern(regexp = PHONE_PATTERN, message = "phone.invalidInput")
    private String phone;
    @Min(value = MIN_RATE, message = "rate.minValue")
    @Max(value = MAX_RATE, message = "rate.maxValue")
    private Byte rate;
    @Positive(message = "page.positive")
    @Max(value = MAX_PAGE_VALUE, message = "page.max")
    private Integer page = DEFAULT_PAGE_VALUE;
    @PositiveOrZero(message = "size.positive")
    @Max(value = MAX_PAGE_SIZE, message = "size.max")
    private Integer size = DEFAULT_PAGE_SIZE;
    @Pattern(regexp = PROFILE_SORT_FIELDS, message = "sort.field.invalidInput")
    private String sortBy = FieldsToSort.FIRST_NAME.toString();
    @Pattern(regexp = ORDER_DIRECTION, message = "order.invalidInput")
    private String order = OrderDirection.ASC.toString();
}
