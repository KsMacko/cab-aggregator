package com.internship.driverservice.util;

import com.internship.driverservice.dto.request.RequestCarDto;
import com.internship.driverservice.dto.response.ResponseCarDto;
import com.internship.driverservice.entity.Car;
import com.internship.driverservice.entity.DriverProfile;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.internship.driverservice.util.UtilConstants.DEFAULT_BRAND;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_CAR_NUMBER;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_COLOR;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_ID;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_IS_CURRENT;
import static com.internship.driverservice.util.UtilConstants.UPDATED_BRAND;
import static com.internship.driverservice.util.UtilConstants.UPDATED_CAR_NUMBER;
import static com.internship.driverservice.util.UtilConstants.UPDATED_COLOR;
import static com.internship.driverservice.util.UtilConstants.UPDATED_IS_CURRENT;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CarUtil {

    public static RequestCarDto requestCarDto() {
        return RequestCarDto.builder()
                .driverId(DEFAULT_ID)
                .isCurrent(DEFAULT_IS_CURRENT)
                .carNumber(DEFAULT_CAR_NUMBER)
                .brand(DEFAULT_BRAND)
                .color(DEFAULT_COLOR)
                .build();
    }

    public static ResponseCarDto responseCarDto() {
        return ResponseCarDto.builder()
                .driverId(DEFAULT_ID)
                .isCurrent(DEFAULT_IS_CURRENT)
                .carNumber(DEFAULT_CAR_NUMBER)
                .brand(DEFAULT_BRAND)
                .color(DEFAULT_COLOR)
                .build();
    }

    public static Car carEntity() {
        return Car.builder()
                .carNumber(DEFAULT_CAR_NUMBER)
                .brand(DEFAULT_BRAND)
                .color(DEFAULT_COLOR)
                .driverProfile(createDriverProfile())
                .isCurrent(DEFAULT_IS_CURRENT)
                .build();
    }
    public static Car carNotCurrentEntity() {
        return Car.builder()
                .id(DEFAULT_ID)
                .carNumber(DEFAULT_CAR_NUMBER)
                .brand(DEFAULT_BRAND)
                .color(DEFAULT_COLOR)
                .driverProfile(createDriverProfile())
                .isCurrent(!DEFAULT_IS_CURRENT)
                .build();
    }

    public static DriverProfile createDriverProfile() {
        return DriverProfile.builder()
                .profileId(DEFAULT_ID)
                .build();
    }
}