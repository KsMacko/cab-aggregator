package com.internship.rideservice.dto.response;

import com.internship.rideservice.enums.FareType;
import com.internship.rideservice.enums.PaymentType;
import com.internship.rideservice.enums.RideStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Builder
public record ResponseRideDto(
        String id,
        Long driverId,
        Long passengerId,
        String promoCode,
        String startLocation,
        List<String> endLocation,
        LocalDateTime createdAt,
        LocalTime startWaitingTime,
        LocalTime startTime,
        LocalTime endTime,
        Float distance,
        RideStatus status,
        FareType fareType,
        String price,
        PaymentType paymentType
) {
}
