package com.internship.rideservice.utils;

import com.internship.commonevents.event.CashConfirmationRequest;
import com.internship.commonevents.event.ChangeRideStatusEvent;
import com.internship.rideservice.dto.request.RequestRideDto;
import com.internship.rideservice.dto.response.ResponseRideDto;
import com.internship.rideservice.dto.transfer.RideFilterRequest;
import com.internship.rideservice.dto.transfer.RidePackageDto;
import com.internship.rideservice.entity.Ride;
import com.internship.rideservice.enums.FareType;
import com.internship.rideservice.enums.PaymentType;
import com.internship.rideservice.enums.RideStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RideUtil implements UtilConstants {

    public static RequestRideDto validRideDto() {
        return RequestRideDto.builder()
                .passengerId(VALID_PERSON_ID)
                .promoCode(VALID_PROMO_CODE)
                .startLocation(VALID_START_LOCATION)
                .endLocation(VALID_END_LOCATIONS)
                .distance(VALID_DISTANCE)
                .fareType(VALID_FARE_TYPE)
                .paymentType(VALID_PAYMENT_TYPE)
                .build();
    }

    public static ResponseRideDto responseRideDto() {
        return ResponseRideDto.builder()
                .id(VALID_ID)
                .passengerId(VALID_PERSON_ID)
                .driverId(VALID_PERSON_ID)
                .promoCode(VALID_PROMO_CODE)
                .startLocation(VALID_START_LOCATION)
                .endLocation(VALID_END_LOCATIONS)
                .status(RideStatus.valueOf(VALID_RIDE_STATUS))
                .fareType(FareType.valueOf(VALID_FARE_TYPE))
                .paymentType(PaymentType.valueOf(VALID_PAYMENT_TYPE))
                .distance(VALID_DISTANCE)
                .createdAt(LocalDateTime.now())
                .startWaitingTime(LocalTime.now().plusMinutes(3))
                .startTime(LocalTime.now().plusMinutes(4))
                .endTime(LocalTime.now().plusMinutes(10))
                .price(VALID_PRICE.toString())
                .build();
    }

    public static Ride rideEntity() {
        return Ride.builder()
                .id(VALID_ID)
                .passengerId(VALID_PERSON_ID)
                .driverId(VALID_PERSON_ID)
                .promoCode(VALID_PROMO_CODE)
                .startLocation(VALID_START_LOCATION)
                .endLocation(VALID_END_LOCATIONS)
                .distance(VALID_DISTANCE)
                .status(RideStatus.valueOf(VALID_RIDE_STATUS))
                .fareType(FareType.valueOf(VALID_FARE_TYPE))
                .paymentType(PaymentType.valueOf(VALID_PAYMENT_TYPE))
                .createdAt(LocalDateTime.now())
                .startWaitingTime(LocalTime.now().plusMinutes(3))
                .startTime(LocalTime.now().plusMinutes(4))
                .endTime(LocalTime.now().plusMinutes(10))
                .price(VALID_PRICE.toString())
                .build();
    }

    public static RideFilterRequest validRideFilterRequest() {
        return RideFilterRequest.builder()
                .page(DEFAULT_PAGE_NUMBER)
                .size(DEFAULT_PAGE_SIZE)
                .sortBy(RIDE_SORT_FIELD)
                .order(DEFAULT_ORDER)
                .build();
    }

    public static RidePackageDto ridePackageDto() {
        return RidePackageDto.builder()
                .ridesDto(List.of(responseRideDto()))
                .totalElements(1)
                .pageNumber(DEFAULT_PAGE_NUMBER)
                .pageSize(1)
                .totalPages(1)
                .build();
    }
    public static ChangeRideStatusEvent changeRideStatusEvent() {
        return ChangeRideStatusEvent.builder()
                .rideId(VALID_ID)
                .driverId(VALID_PERSON_ID)
                .status(VALID_RIDE_STATUS)
                .build();
    }
    public static CashConfirmationRequest cashConfirmationRequest(){
        return CashConfirmationRequest.builder()
                .passengerId(VALID_PERSON_ID)
                .driverId(VALID_PERSON_ID)
                .rideId(VALID_ID)
                .amount(VALID_PRICE)
                .build();
    }
}