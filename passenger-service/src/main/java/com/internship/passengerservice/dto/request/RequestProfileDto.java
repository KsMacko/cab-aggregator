package com.internship.passengerservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import static com.internship.passengerservice.utils.ValidationConstants.CYRILLIC_REGEX;
import static com.internship.passengerservice.utils.ValidationConstants.EMAIL_PATTERN;
import static com.internship.passengerservice.utils.ValidationConstants.MAX_EMAIL_LENGTH;
import static com.internship.passengerservice.utils.ValidationConstants.MAX_NAME_LENGTH;
import static com.internship.passengerservice.utils.ValidationConstants.PHONE_PATTERN;

@Builder
public record RequestProfileDto(
        @Pattern(regexp = CYRILLIC_REGEX, message = "firstName.invalidInput")
        @Size(max = MAX_NAME_LENGTH, message = "firstName.size")
        @NotBlank(message = "firstName.notBlank")
        String firstName,
        @Pattern(regexp = EMAIL_PATTERN, message = "email.invalidInput")
        @Size(max = MAX_EMAIL_LENGTH, message = "email.invalidSize")
        @NotBlank(message = "email.notBlank")
        String email,
        @Pattern(regexp = PHONE_PATTERN, message = "phone.invalidInput")
        @NotBlank(message = "phone.notBlank")
        String phone
){

}
