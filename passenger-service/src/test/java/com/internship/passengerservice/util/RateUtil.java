package com.internship.passengerservice.util;

import com.internship.passengerservice.dto.request.RequestRateDto;
import com.internship.passengerservice.dto.response.ResponseRateDto;
import com.internship.passengerservice.entity.Rate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RateUtil implements UtilConstants{
    public static RequestRateDto validRateDto() {
        return RequestRateDto.builder()
                .value(VALID_RATE)
                .authorId(VALID_ID)
                .recipientId(VALID_ID)
                .rideId(VALID_RIDE_ID)
                .build();
    }

    public static Rate validRateEntity() {
        return Rate.builder()
                .value(VALID_RATE)
                .authorId(VALID_ID)
                .rideId(VALID_RIDE_ID)
                .passenger(ProfileUtil.validPassengerProfile())
                .build();
    }

    public static ResponseRateDto responseRateDto() {
        return ResponseRateDto.builder()
                .id(VALID_ID)
                .value(VALID_RATE)
                .authorId(VALID_ID)
                .rideId(VALID_RIDE_ID)
                .recipientId(VALID_ID)
                .build();
    }
}