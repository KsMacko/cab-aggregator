package com.internship.driverservice.unit.mapper;

import com.internship.driverservice.dto.mapper.NotificationMapper;
import com.internship.driverservice.dto.response.PaymentByCashConfirmationDto;
import com.internship.driverservice.dto.response.RideCreatedNotificationDto;
import com.internship.driverservice.entity.PaymentByCashConfirmation;
import com.internship.driverservice.entity.RideCreationNotification;
import com.internship.driverservice.util.NotificationUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("NotificationMapper unit tests")
public class NotificationMapperTest {

    private final NotificationMapper mapper = Mappers.getMapper(NotificationMapper.class);

    @Test
    @DisplayName("handleEntity maps RideCreationNotification to RideCreatedNotificationDto")
    void handleRideCreationNotificationEntity() {
        RideCreationNotification entity = NotificationUtil.rideCreationNotification();
        RideCreatedNotificationDto dto = mapper.handleEntity(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(entity.getId());
        assertThat(dto.rideId()).isEqualTo(entity.getNotification().getRideId());
        assertThat(dto.type()).isEqualTo(entity.getNotification().getType());
        assertThat(dto.status()).isEqualTo(entity.getNotification().getStatus());
        assertThat(dto.activity()).isEqualTo(entity.getNotification().getActivity());
        assertThat(dto.createdAt()).isEqualTo(entity.getNotification().getCreatedAt());
        assertThat(dto.updatedAt()).isEqualTo(entity.getNotification().getUpdatedAt());
        assertThat(dto.startLocation()).isEqualTo(entity.getStartLocation());
        assertThat(dto.endLocations()).isEqualTo(entity.getEndLocations());
    }

    @Test
    @DisplayName("handleEntity maps PaymentByCashConfirmation to PaymentByCashConfirmationDto")
    void handlePaymentByCashConfirmationEntity() {
        PaymentByCashConfirmation entity = NotificationUtil.paymentByCashConfirmation();
        PaymentByCashConfirmationDto dto = mapper.handleEntity(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(entity.getId());
        assertThat(dto.rideId()).isEqualTo(entity.getNotification().getRideId());
        assertThat(dto.type()).isEqualTo(entity.getNotification().getType());
        assertThat(dto.status()).isEqualTo(entity.getNotification().getStatus());
        assertThat(dto.activity()).isEqualTo(entity.getNotification().getActivity());
        assertThat(dto.createdAt()).isEqualTo(entity.getNotification().getCreatedAt());
        assertThat(dto.updatedAt()).isEqualTo(entity.getNotification().getUpdatedAt());
        assertThat(dto.amount()).isEqualTo(entity.getAmount());
        assertThat(dto.passengerId()).isEqualTo(entity.getPassengerId());
    }
}
