package com.internship.driverservice.unit.mapper;

import com.internship.driverservice.dto.mapper.RateMapper;
import com.internship.driverservice.dto.request.RequestRateDto;
import com.internship.driverservice.dto.response.ResponseRateDto;
import com.internship.driverservice.entity.Rate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.internship.driverservice.util.RateUtil.rateEntity;
import static com.internship.driverservice.util.RateUtil.requestRateDto;
import static com.internship.driverservice.util.RateUtil.responseRateDto;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RateMapper unit tests")
public class RateMapperTest {

    private final RateMapper rateMapper = Mappers.getMapper(RateMapper.class);

    @Test
    @DisplayName("handleDto maps RequestRateDto to Rate entity")
    void handleDto() {
        RequestRateDto dto = requestRateDto();
        Rate entity = rateMapper.handleDto(dto);
        Rate expectedEntity = rateEntity();

        assertThat(entity)
                .usingRecursiveComparison()
                .comparingOnlyFields("value", "authorId", "rideId")
                .isEqualTo(expectedEntity);
    }

    @Test
    @DisplayName("handleEntity maps Rate entity to ResponseRateDto")
    void handleEntity() {
        Rate entity = rateEntity();
        ResponseRateDto dto = rateMapper.handleEntity(entity);
        ResponseRateDto expectedDto = responseRateDto();

        assertThat(dto)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(expectedDto);
    }
}