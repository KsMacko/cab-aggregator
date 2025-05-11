package com.internship.driverservice.util;

import com.internship.commonevents.event.RideParticipantsConfirmation;
import com.internship.driverservice.dto.request.RequestRateDto;
import com.internship.driverservice.dto.response.ResponseRateDto;
import com.internship.driverservice.entity.DriverProfile;
import com.internship.driverservice.entity.Rate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RateUtil {

    public static final Integer DEFAULT_RATE_VALUE = 5;
    public static final Long DEFAULT_AUTHOR_ID = 1L;
    public static final Long DEFAULT_RATE_ID = 1L;
    public static final Long DEFAULT_RECIPIENT_ID = 2L;
    public static final String DEFAULT_RIDE_ID = "ride123";

    public static RequestRateDto requestRateDto() {
        return RequestRateDto.builder()
                .value(DEFAULT_RATE_VALUE)
                .authorId(DEFAULT_AUTHOR_ID)
                .recipientId(DEFAULT_RECIPIENT_ID)
                .rideId(DEFAULT_RIDE_ID)
                .build();
    }

    public static ResponseRateDto responseRateDto() {
        return ResponseRateDto.builder()
                .id(DEFAULT_RATE_ID)
                .value(DEFAULT_RATE_VALUE)
                .authorId(DEFAULT_AUTHOR_ID)
                .recipientId(DEFAULT_RECIPIENT_ID)
                .createdAt(LocalDateTime.now().minusMinutes(5))
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static Rate rateEntity() {
        return Rate.builder()
                .id(DEFAULT_RATE_ID)
                .value(DEFAULT_RATE_VALUE)
                .authorId(DEFAULT_AUTHOR_ID)
                .rideId(DEFAULT_RIDE_ID)
                .driver(DriverProfile.builder()
                        .profileId(DEFAULT_RECIPIENT_ID)
                        .build())
                .createdAt(LocalDateTime.now().minusMinutes(5))
                .updatedAt(LocalDateTime.now())
                .build();
    }
    public static RideParticipantsConfirmation validRideConfirmation(Long driverId) {
        return new RideParticipantsConfirmation(driverId, DEFAULT_RECIPIENT_ID);
    }
}