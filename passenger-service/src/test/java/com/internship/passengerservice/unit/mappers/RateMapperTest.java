package com.internship.passengerservice.unit.mappers;

import com.internship.passengerservice.dto.mapper.RateMapper;
import com.internship.passengerservice.dto.request.RequestRateDto;
import com.internship.passengerservice.dto.response.ResponseRateDto;
import com.internship.passengerservice.entity.Rate;
import com.internship.passengerservice.util.RateUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class RateMapperTest {
    private static final RateMapper rateMapper = Mappers.getMapper(RateMapper.class);
    private static final RequestRateDto request = RateUtil.validRateDto();

    @Test
    void shouldMapRequestDtoToEntity() {
        Rate entity = rateMapper.handleDto(request);

        assertThat(entity).isNotNull();
        assertThat(entity.getValue()).isEqualTo(request.value());
        assertThat(entity.getAuthorId()).isEqualTo(request.authorId());
        assertThat(entity.getRideId()).isEqualTo(request.rideId());
        assertThat(entity.getId()).isNull();
        assertThat(entity.getCreatedAt()).isNull();
        assertThat(entity.getUpdatedAt()).isNull();
        assertThat(entity.getPassenger()).isNull();
    }

    @Test
    void shouldMapEntityToResponseDto() {
        Rate entity = RateUtil.validRateEntity();

        ResponseRateDto response = rateMapper.handleEntity(entity);

        assertThat(response).isNotNull();
        assertThat(response.value()).isEqualTo(entity.getValue());
        assertThat(response.authorId()).isEqualTo(entity.getAuthorId());
        assertThat(response.id()).isEqualTo(entity.getId());
        assertThat(response.createdAt()).isEqualTo(entity.getCreatedAt());
        assertThat(response.updatedAt()).isEqualTo(entity.getUpdatedAt());
    }
}
