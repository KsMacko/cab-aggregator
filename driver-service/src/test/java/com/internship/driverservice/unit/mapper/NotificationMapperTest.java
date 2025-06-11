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

import static com.internship.driverservice.util.NotificationUtil.paymentByCashConfirmationDto;
import static com.internship.driverservice.util.NotificationUtil.rideCreatedNotificationDto;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("NotificationMapper unit tests")
public class NotificationMapperTest {

    private final NotificationMapper mapper = Mappers.getMapper(NotificationMapper.class);

    @Test
    @DisplayName("handleEntity maps RideCreationNotification to RideCreatedNotificationDto")
    void handleRideCreationNotificationEntity() {
        RideCreationNotification entity = NotificationUtil.rideCreationNotification();
        RideCreatedNotificationDto dto = mapper.handleEntity(entity);
        RideCreatedNotificationDto expectedResult = rideCreatedNotificationDto();

        assertThat(dto)
                .usingRecursiveComparison()
                .isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("handleEntity maps PaymentByCashConfirmation to PaymentByCashConfirmationDto")
    void handlePaymentByCashConfirmationEntity() {
        PaymentByCashConfirmation entity = NotificationUtil.paymentByCashConfirmation();
        PaymentByCashConfirmationDto dto = mapper.handleEntity(entity);
        PaymentByCashConfirmationDto expectedResult = paymentByCashConfirmationDto();

        assertThat(dto)
                .usingRecursiveComparison()
                .isEqualTo(expectedResult);
    }
}
