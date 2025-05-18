package com.internship.rideservice.unit.mappers;

import com.internship.rideservice.dto.mapper.FareMapper;
import com.internship.rideservice.dto.request.RequestFareDto;
import com.internship.rideservice.dto.response.ResponseFareDto;
import com.internship.rideservice.entity.Fare;
import com.internship.rideservice.enums.FareType;
import com.internship.rideservice.utils.FareUtil;
import com.internship.rideservice.utils.UtilConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FareMapperTest implements UtilConstants {

    private static final FareMapper fareMapper = Mappers.getMapper(FareMapper.class);

    @Test
    void handleFareDto_shouldMapToEntity() {
        RequestFareDto dto = FareUtil.validFareDto();

        Fare result = fareMapper.handleDto(dto);

        assertThat(result).isNotNull();
        assertThat(result.getType()).isEqualTo(FareType.valueOf(dto.type()));
        assertThat(result.getMinPrice()).isEqualTo(dto.minPrice());
        assertThat(result.getFreeWaiting()).isEqualTo(dto.freeWaiting());
        assertThat(result.getPaidWaitingPrice()).isEqualTo(dto.paidWaitingPrice());
        assertThat(result.getPricePerKm()).isEqualTo(dto.pricePerKm());
        assertThat(result.getPricePerMin()).isEqualTo(dto.pricePerMin());
        assertThat(result.getCreatedAt()).isNull();
    }

    @Test
    void handleFareEntity_shouldMapToResponseFareDto() {
        Fare entity = FareUtil.fareEntity();

        ResponseFareDto result = fareMapper.handleEntity(entity);

        assertThat(result).isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("createdAt")
                .isEqualTo(FareUtil.responseFareDto());
    }

}