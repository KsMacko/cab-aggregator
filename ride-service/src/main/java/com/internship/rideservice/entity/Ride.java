package com.internship.rideservice.entity;

import com.internship.rideservice.enums.FareType;
import com.internship.rideservice.enums.PaymentType;
import com.internship.rideservice.enums.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Document(collection = "rides")
@Getter
@Setter
@FieldNameConstants
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ride {
    @Id
    private String id;
    private Long passengerId;
    private Long driverId;
    private String promoCode;
    private String startLocation;
    private List<String> endLocation;
    private LocalDateTime createdAt;
    private LocalTime startWaitingTime;
    private LocalTime startTime;
    private LocalTime endTime;
    private Float distance;
    private RideStatus status;
    private FareType fareType;
    private String price;
    private PaymentType paymentType;
}
