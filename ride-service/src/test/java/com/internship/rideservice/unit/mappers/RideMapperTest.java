package com.internship.rideservice.unit.mappers;

import com.internship.rideservice.dto.mapper.RideMapper;
import com.internship.rideservice.dto.response.ResponseRideDto;
import com.internship.rideservice.entity.Ride;
import com.internship.rideservice.utils.RideUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.internship.rideservice.utils.RideUtil.rideEntity;
import static com.internship.rideservice.utils.RideUtil.validRideDto;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class RideMapperTest {

    private static final RideMapper rideMapper = Mappers.getMapper(RideMapper.class);

    @Test
    void handleDto_shouldMapCorrectly_whenValidRequest() {
        Ride result = rideMapper.handleDto(validRideDto());
        Ride expectedRide = rideEntity();

        Assertions.assertThat(result)
                .usingRecursiveComparison()
                .comparingOnlyFields(
                        "passengerId",
                        "promoCode",
                        "startLocation",
                        "endLocation",
                        "distance",
                        "fareType",
                        "paymentType")
                .isEqualTo(expectedRide);
    }

    @Test
    void handleEntity_shouldMapToResponseDto_whenValidEntity() {
        ResponseRideDto expected = RideUtil.responseRideDto();
        ResponseRideDto result = rideMapper.handleEntity(rideEntity());

        assertThat(result).usingRecursiveComparison()
                .ignoringFields("createdAt", "startWaitingTime", "startTime", "endTime")
                .isEqualTo(expected);
    }
}