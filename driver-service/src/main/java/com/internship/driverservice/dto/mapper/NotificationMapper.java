package com.internship.driverservice.dto.mapper;

import com.internship.driverservice.dto.response.PaymentByCashConfirmationDto;
import com.internship.driverservice.dto.response.RideCreatedNotificationDto;
import com.internship.driverservice.entity.Notification;
import com.internship.driverservice.entity.PaymentByCashConfirmation;
import com.internship.driverservice.entity.RideCreationNotification;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificationMapper {
    @Mapping(target = "rideId", source = "notification.rideId")
    @Mapping(target = "type", source = "notification.type")
    @Mapping(target = "status", source = "notification.status")
    @Mapping(target = "activity", source = "notification.activity")
    @Mapping(target = "createdAt", source = "notification.createdAt")
    @Mapping(target = "updatedAt", source = "notification.updatedAt")
    PaymentByCashConfirmationDto handleEntity(PaymentByCashConfirmation entity);

    @Mapping(target = "rideId", source = "notification.rideId")
    @Mapping(target = "type", source = "notification.type")
    @Mapping(target = "status", source = "notification.status")
    @Mapping(target = "activity", source = "notification.activity")
    @Mapping(target = "createdAt", source = "notification.createdAt")
    @Mapping(target = "updatedAt", source = "notification.updatedAt")
    RideCreatedNotificationDto handleEntity(RideCreationNotification entity);
}
