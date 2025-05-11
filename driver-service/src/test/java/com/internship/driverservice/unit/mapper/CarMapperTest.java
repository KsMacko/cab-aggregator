package com.internship.driverservice.unit.mapper;

import com.internship.driverservice.dto.mapper.CarMapper;
import com.internship.driverservice.dto.request.RequestCarDto;
import com.internship.driverservice.dto.response.ResponseCarDto;
import com.internship.driverservice.entity.Car;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static com.internship.driverservice.util.CarUtil.carEntity;
import static com.internship.driverservice.util.CarUtil.requestCarDto;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CarMapper unit tests")
public class CarMapperTest {

    private final CarMapper carMapper = Mappers.getMapper(CarMapper.class);

    @Test
    @DisplayName("handleDto maps RequestCarDto to Car entity")
    void handleDto() {
        RequestCarDto dto = requestCarDto();
        Car entity = carMapper.handleDto(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getCarNumber()).isEqualTo(dto.carNumber());
        assertThat(entity.getBrand()).isEqualTo(dto.brand());
        assertThat(entity.getColor()).isEqualTo(dto.color());
        assertThat(entity.getIsCurrent()).isEqualTo(dto.isCurrent());
        assertThat(entity.getDriverProfile()).isNull();
        assertThat(entity.getId()).isNull();
    }

    @Test
    @DisplayName("handleEntity maps Car entity to ResponseCarDto")
    void handleEntity() {
        Car entity = carEntity();
        ResponseCarDto dto = carMapper.handleEntity(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(entity.getId());
        assertThat(dto.driverId()).isEqualTo(entity.getDriverProfile().getProfileId());
        assertThat(dto.isCurrent()).isEqualTo(entity.getIsCurrent());
        assertThat(dto.carNumber()).isEqualTo(entity.getCarNumber());
        assertThat(dto.brand()).isEqualTo(entity.getBrand());
        assertThat(dto.color()).isEqualTo(entity.getColor());
    }
}