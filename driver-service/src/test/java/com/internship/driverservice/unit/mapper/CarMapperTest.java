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
import static com.internship.driverservice.util.CarUtil.responseCarDto;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CarMapper unit tests")
public class CarMapperTest {

    private final CarMapper carMapper = Mappers.getMapper(CarMapper.class);

    @Test
    @DisplayName("handleDto maps RequestCarDto to Car entity")
    void handleDto() {
        RequestCarDto dto = requestCarDto();
        Car entity = carMapper.handleDto(dto);
        Car expectedCar = carEntity();

        assertThat(entity)
                .usingRecursiveComparison()
                .ignoringFields("id", "driverProfile")
                .isEqualTo(expectedCar);
    }

    @Test
    @DisplayName("handleEntity maps Car entity to ResponseCarDto")
    void handleEntity() {
        Car entity = carEntity();
        ResponseCarDto dto = carMapper.handleEntity(entity);
        ResponseCarDto expectedResponse = responseCarDto();

        assertThat(dto)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedResponse);
    }
}