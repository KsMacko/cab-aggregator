package com.internship.passengerservice.unit.mappers;

import com.internship.passengerservice.dto.mapper.ProfileMapper;
import com.internship.passengerservice.dto.request.RequestProfileDto;
import com.internship.passengerservice.dto.response.ResponseProfileDto;
import com.internship.passengerservice.entity.PassengerProfile;
import com.internship.passengerservice.util.ProfileUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
class ProfileMapperTest {

    private static final ProfileMapper profileMapper = Mappers.getMapper(ProfileMapper.class);
    private static final RequestProfileDto request = ProfileUtil.validProfileDto();
    private static final PassengerProfile entity = ProfileUtil.validPassengerProfile();
    private static final ResponseProfileDto response = ProfileUtil.responseProfileDto();

    @Test
    void shouldMapRequestDtoToEntity() {
        PassengerProfile profile = profileMapper.handleDto(request);

        assertThat(profile)
                .usingRecursiveComparison()
                .ignoringFields("profileId")
                .isEqualTo(entity);
    }

    @Test
    void shouldMapEntityToResponseDto() {
        ResponseProfileDto response = profileMapper.handleEntity(entity);
        assertThat(response)
                .usingRecursiveComparison()
//                .ignoringFields("id", "driverProfile")
                .isEqualTo(response);

    }
}
