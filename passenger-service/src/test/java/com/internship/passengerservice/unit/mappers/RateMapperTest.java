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
    private static final Rate entity = RateUtil.validRateEntity();
    private static final ResponseRateDto response = RateUtil.responseRateDto();

    @Test
    void shouldMapRequestDtoToEntity() {
        Rate result = rateMapper.handleDto(request);

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id", "passenger")
                .isEqualTo(entity);
    }

    @Test
    void shouldMapEntityToResponseDto() {
        ResponseRateDto result = rateMapper.handleEntity(entity);

        assertThat(result)
                .usingRecursiveComparison()
//                .ignoringFields("profileId")
                .isEqualTo(response);
    }
}
