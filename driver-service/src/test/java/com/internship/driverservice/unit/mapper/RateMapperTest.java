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
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RateMapper unit tests")
public class RateMapperTest {

    private final RateMapper rateMapper = Mappers.getMapper(RateMapper.class);

    @Test
    @DisplayName("handleDto maps RequestRateDto to Rate entity")
    void handleDto() {
        RequestRateDto dto = requestRateDto();
        Rate entity = rateMapper.handleDto(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getValue()).isEqualTo(dto.value());
        assertThat(entity.getAuthorId()).isEqualTo(dto.authorId());
        assertThat(entity.getRideId()).isEqualTo(dto.rideId());
        assertThat(entity.getId()).isNull();
        assertThat(entity.getCreatedAt()).isNull();
        assertThat(entity.getUpdatedAt()).isNull();
        assertThat(entity.getDriver()).isNull();
    }

    @Test
    @DisplayName("handleEntity maps Rate entity to ResponseRateDto")
    void handleEntity() {
        Rate entity = rateEntity();
        ResponseRateDto dto = rateMapper.handleEntity(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(entity.getId());
        assertThat(dto.value()).isEqualTo(entity.getValue());
        assertThat(dto.authorId()).isEqualTo(entity.getAuthorId());
        assertThat(dto.recipientId()).isEqualTo(entity.getDriver().getProfileId());
        assertThat(dto.createdAt()).isEqualTo(entity.getCreatedAt());
        assertThat(dto.updatedAt()).isEqualTo(entity.getUpdatedAt());
    }
}