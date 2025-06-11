package com.internship.rideservice.unit.mappers;

import com.internship.rideservice.dto.mapper.FareMapper;
import com.internship.rideservice.dto.response.ResponseFareDto;
import com.internship.rideservice.entity.Fare;
import com.internship.rideservice.utils.FareUtil;
import com.internship.rideservice.utils.UtilConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.internship.rideservice.utils.FareUtil.fareEntity;
import static com.internship.rideservice.utils.FareUtil.responseFareDto;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FareMapperTest implements UtilConstants {

    private static final FareMapper fareMapper = Mappers.getMapper(FareMapper.class);

    @Test
    void handleFareDto_shouldMapToEntity() {
        Fare result = fareMapper.handleDto(FareUtil.validFareDto());
        Fare expectedFare = fareEntity();

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expectedFare);
    }

    @Test
    void handleFareEntity_shouldMapToResponseFareDto() {
        ResponseFareDto result = fareMapper.handleEntity(fareEntity());
        ResponseFareDto expectedResponse = responseFareDto();

        assertThat(result).isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

}