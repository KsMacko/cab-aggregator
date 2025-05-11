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

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProfileUtil {

    public static final String DEFAULT_FIRST_NAME = "Иван";
    public static final String DEFAULT_LAST_NAME = "Иванов";
    public static final String DEFAULT_FARE_TYPE = "ECONOMY";
    public static final String DEFAULT_PHONE = "+375336666666";
    public static final Long DEFAULT_PROFILE_ID = 1L;
    public static final Integer DEFAULT_RATE = 5;
    public static final String DEFAULT_DRIVER_STATUS = "FREE";
    public static final String DEFAULT_FARE = "COMFORT";

    public static final String UPDATED_FIRST_NAME = "Петр";
    public static final String UPDATED_LAST_NAME = "Петров";
    public static final String UPDATED_FARE_TYPE = "COMFORT";
    public static final String UPDATED_PHONE = "+375337777777";

    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final String DEFAULT_SORT_FIELD = FIRST_NAME.toString();
    public static final String DEFAULT_SORT_DIRECTION = ASC.toString();

    public static RequestProfileDto requestProfileDto() {
        return RequestProfileDto.builder()
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .fareType(DEFAULT_FARE_TYPE)
                .phone(DEFAULT_PHONE)
                .build();
    }

    public static RequestProfileDto updatedRequestProfileDto() {
        return RequestProfileDto.builder()
                .firstName(UPDATED_FIRST_NAME)
                .lastName(UPDATED_LAST_NAME)
                .fareType(UPDATED_FARE_TYPE)
                .phone(UPDATED_PHONE)
                .build();
    }

    public static ResponseProfileDto responseProfileDto() {
        return ResponseProfileDto.builder()
                .profileId(DEFAULT_PROFILE_ID)
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .fareType(DEFAULT_FARE_TYPE)
                .driverStatus(DEFAULT_DRIVER_STATUS)
                .phone(DEFAULT_PHONE)
                .rate(DEFAULT_RATE)
                .build();
    }

    public static DriverProfile driverProfile() {
        return DriverProfile.builder()
                .profileId(DEFAULT_PROFILE_ID)
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .fareType(FareType.valueOf(DEFAULT_FARE_TYPE))
                .phone(DEFAULT_PHONE)
                .driverStatus(DriverStatus.valueOf(DEFAULT_DRIVER_STATUS))
                .rates(Collections.emptyList())
                .build();
    }

    public static void updateEntity(DriverProfile entity) {
        entity.setFirstName(UPDATED_FIRST_NAME);
        entity.setLastName(UPDATED_LAST_NAME);
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