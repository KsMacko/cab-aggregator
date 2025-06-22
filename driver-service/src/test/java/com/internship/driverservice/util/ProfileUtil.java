package com.internship.driverservice.util;

import com.internship.driverservice.dto.request.RequestProfileDto;
import com.internship.driverservice.dto.response.ResponseProfileDto;
import com.internship.driverservice.dto.transfer.DriverFilterRequest;
import com.internship.driverservice.entity.DriverProfile;
import com.internship.driverservice.enums.DriverStatus;
import com.internship.driverservice.enums.FareType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;

import static com.internship.driverservice.enums.FieldFilter.FIRST_NAME;
import static com.internship.driverservice.enums.OrderDirection.ASC;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_DRIVER_STATUS;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_FARE_TYPE;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_ID;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_PAGE;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_PAGE_SIZE;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_PHONE;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_RATE;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_STR;
import static com.internship.driverservice.util.UtilConstants.UPDATED_FARE_TYPE;
import static com.internship.driverservice.util.UtilConstants.UPDATED_PHONE;
import static com.internship.driverservice.util.UtilConstants.UPDATED_STR;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProfileUtil {

    public static RequestProfileDto requestProfileDto() {
        return RequestProfileDto.builder()
                .firstName(DEFAULT_STR)
                .lastName(DEFAULT_STR)
                .fareType(DEFAULT_FARE_TYPE)
                .phone(DEFAULT_PHONE)
                .build();
    }

    public static RequestProfileDto updatedRequestProfileDto() {
        return RequestProfileDto.builder()
                .firstName(UPDATED_STR)
                .lastName(UPDATED_STR)
                .fareType(UPDATED_FARE_TYPE)
                .phone(UPDATED_PHONE)
                .build();
    }

    public static ResponseProfileDto responseProfileDto() {
        return ResponseProfileDto.builder()
                .profileId(DEFAULT_ID)
                .firstName(DEFAULT_STR)
                .lastName(DEFAULT_STR)
                .fareType(DEFAULT_FARE_TYPE)
                .driverStatus(DEFAULT_DRIVER_STATUS)
                .phone(DEFAULT_PHONE)
                .rate(DEFAULT_RATE)
                .build();
    }

    public static DriverProfile driverProfile() {
        return DriverProfile.builder()
                .firstName(DEFAULT_STR)
                .lastName(DEFAULT_STR)
                .fareType(FareType.valueOf(DEFAULT_FARE_TYPE))
                .phone(DEFAULT_PHONE)
                .driverStatus(DriverStatus.valueOf(DEFAULT_DRIVER_STATUS))
                .rates(Collections.emptyList())
                .build();
    }

    public static void updateEntity(DriverProfile entity) {
        entity.setFirstName(UPDATED_STR);
        entity.setLastName(UPDATED_STR);
        entity.setFareType(FareType.valueOf(UPDATED_FARE_TYPE));
        entity.setPhone(UPDATED_PHONE);
    }
    public static DriverFilterRequest driverFilterRequest() {
        return DriverFilterRequest.builder()
                .page(DEFAULT_PAGE)
                .size(DEFAULT_PAGE_SIZE)
                .order(ASC.toString())
                .sortBy(FIRST_NAME.toString())
                .build();
    }
}