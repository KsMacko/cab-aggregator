package com.internship.rideservice.utils;

import com.internship.rideservice.dto.request.RequestFareDto;
import com.internship.rideservice.dto.response.ResponseFareDto;
import com.internship.rideservice.dto.transfer.FarePackageDto;
import com.internship.rideservice.entity.Fare;
import com.internship.rideservice.enums.FareType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FareUtil implements UtilConstants {

    public static RequestFareDto validFareDto() {
        return RequestFareDto.builder()
                .type(VALID_FARE_TYPE)
                .minPrice(VALID_PRICE)
                .freeWaiting(VALID_INTEGER)
                .paidWaitingPrice(VALID_PRICE)
                .pricePerKm(VALID_PRICE)
                .pricePerMin(VALID_PRICE)
                .build();
    }

    public static ResponseFareDto responseFareDto() {
        return ResponseFareDto.builder()
                .type(VALID_FARE_TYPE)
                .minPrice(VALID_PRICE)
                .freeWaiting(VALID_INTEGER)
                .paidWaitingPrice(VALID_PRICE)
                .pricePerKm(VALID_PRICE)
                .pricePerMin(VALID_PRICE)
                .build();
    }

    public static Fare fareEntity() {
        return Fare.builder()
                .type(FareType.valueOf(VALID_FARE_TYPE))
                .minPrice(VALID_PRICE)
                .freeWaiting(VALID_INTEGER)
                .paidWaitingPrice(VALID_PRICE)
                .pricePerKm(VALID_PRICE)
                .pricePerMin(VALID_PRICE)
                .build();
    }

    public static FarePackageDto farePackageDto() {
        return new FarePackageDto(List.of(responseFareDto()), 1);
    }
}