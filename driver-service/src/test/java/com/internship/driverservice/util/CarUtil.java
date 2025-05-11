package com.internship.driverservice.util;

import com.internship.driverservice.dto.request.RequestCarDto;
import com.internship.driverservice.dto.response.ResponseCarDto;
import com.internship.driverservice.entity.Car;
import com.internship.driverservice.entity.DriverProfile;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CarUtil {
    public static final Long DEFAULT_CAR_ID = 1L;
    public static final Long DEFAULT_DRIVER_ID = 1L;
    public static final String DEFAULT_CAR_NUMBER = "A123AA777";
    public static final String DEFAULT_BRAND = "Toyota";
    public static final String DEFAULT_COLOR = "Черный";
    public static final Boolean DEFAULT_IS_CURRENT = true;

    public static final String UPDATED_CAR_NUMBER = "B456BB777";
    public static final String UPDATED_BRAND = "Honda";
    public static final String UPDATED_COLOR = "Белый";
    public static final Boolean UPDATED_IS_CURRENT = false;

    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int DEFAULT_PAGE_SIZE_FOR_PAGINATION = 20;

    public static RequestCarDto requestCarDto() {
        return RequestCarDto.builder()
                .driverId(DEFAULT_DRIVER_ID)
                .isCurrent(DEFAULT_IS_CURRENT)
                .carNumber(DEFAULT_CAR_NUMBER)
                .brand(DEFAULT_BRAND)
                .color(DEFAULT_COLOR)
                .build();
    }

    public static ResponseCarDto responseCarDto() {
        return ResponseCarDto.builder()
                .driverId(DEFAULT_CAR_ID)
                .isCurrent(UPDATED_IS_CURRENT)
                .carNumber(UPDATED_CAR_NUMBER)
                .brand(UPDATED_BRAND)
                .color(UPDATED_COLOR)
                .build();
    }
    public static RequestCarDto invalidRequestCarDto() {
        return RequestCarDto.builder()
                .driverId(null)
                .carNumber("")
                .brand(null)
                .color("")
                .isCurrent(null)
                .build();
    }

    public static Car carEntity() {
        return Car.builder()
                .id(DEFAULT_CAR_ID)
                .carNumber(DEFAULT_CAR_NUMBER)
                .brand(DEFAULT_BRAND)
                .color(DEFAULT_COLOR)
                .driverProfile(createDriverProfile())
                .isCurrent(DEFAULT_IS_CURRENT)
                .build();
    }
    public static Car carNotCurrentEntity() {
        return Car.builder()
                .id(DEFAULT_CAR_ID+1L)
                .carNumber(DEFAULT_CAR_NUMBER)
                .brand(DEFAULT_BRAND)
                .color(DEFAULT_COLOR)
                .driverProfile(createDriverProfile())
                .isCurrent(!DEFAULT_IS_CURRENT)
                .build();
    }

    public static DriverProfile createDriverProfile() {
        return DriverProfile.builder()
                .profileId(DEFAULT_DRIVER_ID)
                .build();
    }
}