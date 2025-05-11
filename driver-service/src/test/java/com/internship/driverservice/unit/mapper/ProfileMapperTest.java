package com.internship.driverservice.unit.mapper;

import com.internship.driverservice.dto.mapper.ProfileMapper;
import com.internship.driverservice.dto.request.RequestProfileDto;
import com.internship.driverservice.dto.response.ResponseProfileDto;
import com.internship.driverservice.entity.DriverProfile;
import com.internship.driverservice.enums.DriverStatus;
import com.internship.driverservice.enums.FareType;
import com.internship.driverservice.util.ProfileUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProfileMapper unit tests")
public class ProfileMapperTest {

    private final ProfileMapper profileMapper = Mappers.getMapper(ProfileMapper.class);

    @Test
    @DisplayName("handleDto maps RequestProfileDto to DriverProfile with correct fields")
    void handleDto() {
        RequestProfileDto dto = ProfileUtil.requestProfileDto();
        DriverProfile entity = profileMapper.handleDto(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getFirstName()).isEqualTo(dto.firstName());
        assertThat(entity.getLastName()).isEqualTo(dto.lastName());
        assertThat(entity.getFareType()).isEqualTo(FareType.valueOf(dto.fareType()));
        assertThat(entity.getPhone()).isEqualTo(dto.phone());
    }

    @Test
    @DisplayName("handleEntity maps DriverProfile to ResponseProfileDto")
    void handleEntity_withRating() {
        DriverProfile entity = ProfileUtil.driverProfile();
        Integer rating = ProfileUtil.DEFAULT_RATE;
        ResponseProfileDto dto = profileMapper.handleEntity(entity, rating);

        assertThat(dto).isNotNull();
        assertThat(dto.firstName()).isEqualTo(entity.getFirstName());
        assertThat(dto.lastName()).isEqualTo(entity.getLastName());
        assertThat(FareType.valueOf(dto.fareType())).isEqualTo(entity.getFareType());
        assertThat(DriverStatus.valueOf(dto.driverStatus())).isEqualTo(entity.getDriverStatus());
        assertThat(dto.phone()).isEqualTo(entity.getPhone());
    }

    @Test
    @DisplayName("handleEntity maps DriverProfile to ResponseProfileDto")
    void handleEntity_withoutRating() {
        DriverProfile entity = ProfileUtil.driverProfile();
        ResponseProfileDto dto = profileMapper.handleEntity(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.firstName()).isEqualTo(entity.getFirstName());
        assertThat(dto.lastName()).isEqualTo(entity.getLastName());
        assertThat(FareType.valueOf(dto.fareType())).isEqualTo(entity.getFareType());
        assertThat(DriverStatus.valueOf(dto.driverStatus())).isEqualTo(entity.getDriverStatus());
        assertThat(dto.phone()).isEqualTo(entity.getPhone());
        assertThat(dto.rate()).isNull();
    }

    @Test
    @DisplayName("updateProfileFromDto updates only non-null fields in DriverProfile from dto")
    void updateProfileFromDto() {
        DriverProfile entity = ProfileUtil.driverProfile();
        RequestProfileDto dto = ProfileUtil.updatedRequestProfileDto();
        profileMapper.updateProfileFromDto(dto, entity);

        assertThat(entity.getFirstName()).isEqualTo(dto.firstName());
        assertThat(entity.getLastName()).isEqualTo(dto.lastName());
        assertThat(entity.getFareType()).isEqualTo(FareType.valueOf(dto.fareType()));
        assertThat(entity.getPhone()).isEqualTo(dto.phone());
    }
}