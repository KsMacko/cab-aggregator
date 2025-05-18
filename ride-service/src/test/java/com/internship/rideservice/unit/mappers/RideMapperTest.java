package com.internship.rideservice.unit.mappers;

import com.internship.rideservice.dto.mapper.RideMapper;
import com.internship.rideservice.dto.request.RequestRideDto;
import com.internship.rideservice.dto.response.ResponseRideDto;
import com.internship.rideservice.entity.Ride;
import com.internship.rideservice.enums.FareType;
import com.internship.rideservice.enums.PaymentType;
import com.internship.rideservice.enums.RideStatus;
import com.internship.rideservice.utils.RideUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class RideMapperTest {

    private static final RideMapper rideMapper = Mappers.getMapper(RideMapper.class);

    @Test
    void handleDto_shouldMapCorrectly_whenValidRequest() {
        RequestRideDto dto = RideUtil.validRideDto();

        Ride result = rideMapper.handleDto(dto);

        assertThat(result).isNotNull();
        assertThat(result.getPassengerId()).isEqualTo(dto.passengerId());
        assertThat(result.getPromoCode()).isEqualTo(dto.promoCode());
        assertThat(result.getStartLocation()).isEqualTo(dto.startLocation());
        assertThat(result.getEndLocation()).isEqualTo(dto.endLocation());
        assertThat(result.getDistance()).isEqualTo(dto.distance());
        assertThat(result.getFareType()).isEqualTo(FareType.ECONOMY);
        assertThat(result.getStatus()).isEqualTo(RideStatus.CREATED);
        assertThat(result.getPaymentType()).isEqualTo(PaymentType.CASH);
        assertThat(result.getCreatedAt()).isNotNull();

        assertThat(result.getId()).isNull();
        assertThat(result.getDriverId()).isNull();
        assertThat(result.getPrice()).isNull();
        assertThat(result.getStartWaitingTime()).isNull();
        assertThat(result.getStartTime()).isNull();
        assertThat(result.getEndTime()).isNull();
    }

    @Test
    void handleEntity_shouldMapToResponseDto_whenValidEntity() {
        Ride entity = RideUtil.rideEntity();
        ResponseRideDto expected = RideUtil.responseRideDto();

        ResponseRideDto result = rideMapper.handleEntity(entity);

        assertThat(result).usingRecursiveComparison()
                .ignoringFields("createdAt", "startWaitingTime", "startTime", "endTime")
                .isEqualTo(expected);
    }
}