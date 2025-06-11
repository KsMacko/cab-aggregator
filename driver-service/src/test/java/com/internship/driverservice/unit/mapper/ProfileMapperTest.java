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

import static com.internship.driverservice.util.ProfileUtil.driverProfile;
import static com.internship.driverservice.util.ProfileUtil.responseProfileDto;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_RATE;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ProfileMapper unit tests")
public class ProfileMapperTest {

    private final ProfileMapper profileMapper = Mappers.getMapper(ProfileMapper.class);

    @Test
    @DisplayName("handleDto maps RequestProfileDto to DriverProfile with correct fields")
    void handleDto() {
        RequestProfileDto dto = ProfileUtil.requestProfileDto();
        DriverProfile entity = profileMapper.handleDto(dto);
        DriverProfile expectedDriver = driverProfile();

        assertThat(entity)
                .usingRecursiveComparison()
                .ignoringFields("profileId", "driverStatus", "rates")
                .isEqualTo(expectedDriver);
    }

    @Test
    @DisplayName("handleEntity maps DriverProfile to ResponseProfileDto")
    void handleEntity_withRating() {
        DriverProfile entity = driverProfile();
        ResponseProfileDto dto = profileMapper.handleEntity(entity, DEFAULT_RATE);
        ResponseProfileDto expectedDto = responseProfileDto();

        assertThat(dto)
                .usingRecursiveComparison()
                .ignoringFields("rate")
                .isEqualTo(expectedDto);
    }


    @Test
    @DisplayName("updateProfileFromDto updates only non-null fields in DriverProfile from dto")
    void updateProfileFromDto() {
        DriverProfile entity = driverProfile();
        RequestProfileDto dto = ProfileUtil.updatedRequestProfileDto();
        DriverProfile expectedDriver = DriverProfile.builder()
                .profileId(entity.getProfileId())
                .fareType(FareType.valueOf(dto.fareType()))
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .phone(dto.phone())
                .build();
        profileMapper.updateProfileFromDto(dto, entity);

        assertThat(entity)
                .usingRecursiveComparison()
                .comparingOnlyFields("firstName", "lastName", "phone", "fareType")
                .isEqualTo(expectedDriver);
    }
}