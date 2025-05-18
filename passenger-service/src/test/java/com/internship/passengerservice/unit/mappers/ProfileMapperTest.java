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

    @Test
    void shouldMapRequestDtoToEntity() {
        PassengerProfile profile = profileMapper.handleDto(request);

        assertThat(profile).isNotNull();
        assertThat(profile.getFirstName()).isEqualTo(request.firstName());
        assertThat(profile.getEmail()).isEqualTo(request.email());
        assertThat(profile.getPhone()).isEqualTo(request.phone());
        assertThat(profile.getProfileId()).isNull();
        assertThat(profile.getCreatedAt()).isNull();
        assertThat(profile.getUpdatedAt()).isNull();
        assertThat(profile.getRates()).isNull();
    }

    @Test
    void shouldMapEntityToResponseDto() {
        ResponseProfileDto response = profileMapper.handleEntity(entity);

        assertThat(response).isNotNull();
        assertThat(response.profileId()).isEqualTo(entity.getProfileId());
        assertThat(response.firstName()).isEqualTo(entity.getFirstName());
        assertThat(response.email()).isEqualTo(entity.getEmail());
        assertThat(response.phone()).isEqualTo(entity.getPhone());
        assertThat(response.createdAt()).isEqualTo(entity.getCreatedAt());
        assertThat(response.updatedAt()).isEqualTo(entity.getUpdatedAt());
        assertThat(response.rate()).isNull();
    }

    @Test
    void shouldUpdateEntityFromDto() {
        PassengerProfile entity = PassengerProfile.builder().build();

        profileMapper.updateEntity(request, entity);

        assertThat(entity.getFirstName()).isEqualTo(request.firstName());
        assertThat(entity.getEmail()).isEqualTo(request.email());
        assertThat(entity.getPhone()).isEqualTo(request.phone());
        assertThat(entity.getProfileId()).isNull();
        assertThat(entity.getCreatedAt()).isNull();
        assertThat(entity.getUpdatedAt()).isNull();
        assertThat(entity.getRates()).isNull();
    }
}
