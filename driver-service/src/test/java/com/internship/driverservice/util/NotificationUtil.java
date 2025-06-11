package com.internship.driverservice.util;

import com.internship.commonevents.event.RideNotificationEvent;
import com.internship.driverservice.dto.response.PaymentByCashConfirmationDto;
import com.internship.driverservice.dto.response.RideCreatedNotificationDto;
import com.internship.driverservice.entity.Notification;
import com.internship.driverservice.entity.PaymentByCashConfirmation;
import com.internship.driverservice.entity.RideCreationNotification;
import com.internship.driverservice.enums.notification.NotificationActivity;
import com.internship.driverservice.enums.notification.NotificationStatus;
import com.internship.driverservice.enums.notification.NotificationType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.internship.driverservice.util.UtilConstants.DEFAULT_ACTIVITY;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_AMOUNT;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_FARE;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_ID;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_NOTIFICATION_STATUS;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_STR;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_STR_ID;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NotificationUtil {

    public static Notification notification() {
        return Notification.builder()
                .rideId(DEFAULT_STR_ID)
                .status(NotificationStatus.NON_VIEWED)
                .activity(NotificationActivity.ACTIVE)
                .build();
    }

    public static RideCreationNotification rideCreationNotification() {
        Notification notification = notification();
        notification.setType(NotificationType.RIDE_CREATION);
        return RideCreationNotification.builder()
                .startLocation(DEFAULT_STR)
                .endLocations(DEFAULT_STR)
                .notification(notification)
                .build();
    }
    public static PaymentByCashConfirmation paymentByCashConfirmation() {
        Notification notification = notification();
        notification.setType(NotificationType.CASH_CONFIRMATION);
        return PaymentByCashConfirmation.builder()
                .amount(DEFAULT_AMOUNT)
                .passengerId(DEFAULT_ID)
                .notification(notification)
                .build();
    }
    public static RideCreatedNotificationDto rideCreatedNotificationDto(){
        return RideCreatedNotificationDto.builder()
                .rideId(DEFAULT_STR_ID)
                .status(NotificationStatus.valueOf(DEFAULT_NOTIFICATION_STATUS))
                .activity(DEFAULT_ACTIVITY)
                .startLocation(DEFAULT_STR)
                .endLocations(DEFAULT_STR)
                .type(NotificationType.RIDE_CREATION)
                .build();
    }
    public static PaymentByCashConfirmationDto paymentByCashConfirmationDto(){
        return PaymentByCashConfirmationDto.builder()
                .rideId(DEFAULT_STR_ID)
                .status(NotificationStatus.valueOf(DEFAULT_NOTIFICATION_STATUS))
                .activity(DEFAULT_ACTIVITY)
                .passengerId(DEFAULT_ID)
                .type(NotificationType.CASH_CONFIRMATION)
                .amount(DEFAULT_AMOUNT)
                .build();
    }

    public static RideNotificationEvent createRideNotificationEvent() {
        return RideNotificationEvent.builder()
                .rideId(DEFAULT_STR_ID)
                .fare(DEFAULT_FARE)
                .endLocation(List.of(DEFAULT_STR))
                .pickupLocation(DEFAULT_STR)
                .build();
    }
}