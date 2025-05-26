package com.internship.driverservice.util;

import com.internship.commonevents.event.RideParticipantsConfirmation;
import com.internship.driverservice.dto.request.RequestRateDto;
import com.internship.driverservice.dto.response.ResponseRateDto;
import com.internship.driverservice.entity.DriverProfile;
import com.internship.driverservice.entity.Rate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.internship.driverservice.util.UtilConstants.DEFAULT_ID;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_RATE;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_STR_ID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RateUtil {

    public static RequestRateDto requestRateDto() {
        return RequestRateDto.builder()
                .value(DEFAULT_RATE)
                .authorId(DEFAULT_ID)
                .recipientId(DEFAULT_ID)
                .rideId(DEFAULT_STR_ID)
                .build();
    }

    public static ResponseRateDto responseRateDto() {
        return ResponseRateDto.builder()
                .id(DEFAULT_ID)
                .value(DEFAULT_RATE)
                .authorId(DEFAULT_ID)
                .recipientId(DEFAULT_ID)
                .createdAt(LocalDateTime.now().minusMinutes(5))
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static Rate rateEntity() {
        return Rate.builder()
                .id(DEFAULT_ID)
                .value(DEFAULT_RATE)
                .authorId(DEFAULT_ID)
                .rideId(DEFAULT_STR_ID)
                .driver(DriverProfile.builder()
                        .profileId(DEFAULT_ID)
                        .build())
                .createdAt(LocalDateTime.now().minusMinutes(5))
                .updatedAt(LocalDateTime.now())
                .build();
    }
    public static RideParticipantsConfirmation validRideConfirmation(Long driverId, Long passengerId) {
        return new RideParticipantsConfirmation(driverId, passengerId);
    }
}