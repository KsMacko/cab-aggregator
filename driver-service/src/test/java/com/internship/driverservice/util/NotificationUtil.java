package com.internship.driverservice.util;
import com.internship.commonevents.event.RideNotificationEvent;
import com.internship.driverservice.entity.Notification;
import com.internship.driverservice.entity.PaymentByCashConfirmation;
import com.internship.driverservice.entity.RideCreationNotification;
import com.internship.driverservice.enums.notification.NotificationActivity;
import com.internship.driverservice.enums.notification.NotificationStatus;
import com.internship.driverservice.enums.notification.NotificationType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.internship.driverservice.util.ProfileUtil.DEFAULT_FARE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NotificationUtil {

    public static final Long DEFAULT_NOTIFICATION_ID = 1L;
    public static final String DEFAULT_RIDE_ID = "ride123";
    public static final BigDecimal DEFAULT_AMOUNT = new BigDecimal("10.55");
    public static final Long DEFAULT_PASSENGER_ID = 1L;
    public static final String DEFAULT_START_LOCATION = "Ploshcha lenina";
    public static final String DEFAULT_END_LOCATIONS = "Vokzal";
    public static final String DEFAULT_NOTIFICATION_STATUS = "ACCEPTED";

    public static Notification notification() {
        return Notification.builder()
                .rideId(DEFAULT_RIDE_ID)
                .status(NotificationStatus.NON_VIEWED)
                .activity(NotificationActivity.ACTIVE)
                .build();
    }

    public static RideCreationNotification rideCreationNotification() {
        Notification notification = notification();
        notification.setType(NotificationType.RIDE_CREATION);
        return RideCreationNotification.builder()
                .startLocation(DEFAULT_START_LOCATION)
                .endLocations(DEFAULT_END_LOCATIONS)
                .notification(notification)
                .build();
    }
    public static PaymentByCashConfirmation paymentByCashConfirmation() {
        Notification notification = notification();
        notification.setType(NotificationType.CASH_CONFIRMATION);
        return PaymentByCashConfirmation.builder()
                .amount(DEFAULT_AMOUNT)
                .passengerId(DEFAULT_PASSENGER_ID)
                .notification(notification)
                .build();
    }

    public static RideNotificationEvent createRideNotificationEvent() {
        return RideNotificationEvent.builder()
                .rideId(DEFAULT_RIDE_ID)
                .fare(DEFAULT_FARE)
                .endLocation(List.of(DEFAULT_END_LOCATIONS))
                .pickupLocation(DEFAULT_START_LOCATION)
                .build();
    }
}