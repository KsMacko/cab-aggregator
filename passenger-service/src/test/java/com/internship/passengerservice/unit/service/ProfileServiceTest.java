package com.internship.passengerservice.unit.service;

import com.internship.passengerservice.dto.mapper.ProfileMapper;
import com.internship.passengerservice.dto.mapper.RateMapper;
import com.internship.passengerservice.dto.request.RequestProfileDto;
import com.internship.passengerservice.dto.response.ResponseProfileDto;
import com.internship.passengerservice.dto.transfer.DataPackageDto;
import com.internship.passengerservice.dto.transfer.ProfileFilterRequest;
import com.internship.passengerservice.entity.PassengerProfile;
import com.internship.passengerservice.repo.PassengerProfileRepo;
import com.internship.passengerservice.service.CommandPassengerProfileService;
import com.internship.passengerservice.service.ReadPassengerProfileService;
import com.internship.passengerservice.service.specification.PassengerProfileSpecification;
import com.internship.passengerservice.util.ProfileUtil;
import com.internship.passengerservice.util.UtilConstants;
import com.internship.passengerservice.utils.ProfileValidationManager;
import com.internship.passengerservice.utils.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static com.internship.passengerservice.utils.exceptions.ExceptionCodes.PASSENGER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest implements UtilConstants {

    @InjectMocks
    private CommandPassengerProfileService commandPassengerProfileService;

    @InjectMocks
    private ReadPassengerProfileService readPassengerProfileService;

    @Mock
    private PassengerProfileRepo passengerProfileRepo;

    @Mock
    private ProfileValidationManager profileValidationManager;

    @Mock
    private PassengerProfileSpecification passengerProfileSpecification;

    @Mock
    private ProfileMapper profileMapper;

    private static final RequestProfileDto dto = ProfileUtil.validProfileDto();
    private static final PassengerProfile profile = ProfileUtil.validPassengerProfile();

    @Test
    void createNewPassengerProfile_shouldCreateProfile() {

        when(profileMapper.handleDto(dto)).thenReturn(profile);
        when(passengerProfileRepo.save(profile)).thenReturn(profile);
        when(profileMapper.handleEntity(profile)).thenReturn(ProfileUtil.responseProfileDto());

        ResponseProfileDto result = commandPassengerProfileService.createNewPassengerProfile(dto);

        assertThat(result).isNotNull();
        verify(profileValidationManager, times(1)).checkEmailUniqueness(dto.email());
        verify(profileValidationManager, times(1)).checkPhoneNumberUniqueness(dto.phone());
    }

    @Test
    void updatePassengerProfile_shouldUpdate() {

        doNothing().when(profileValidationManager).checkIfProfileExists(anyLong());
        when(profileValidationManager.getProfileByIdIfExists(anyLong())).thenReturn(profile);
        doNothing().when(profileValidationManager).checkProfileToUpdate(dto);

        when(passengerProfileRepo.save(any(PassengerProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PassengerProfile updated = commandPassengerProfileService.updatePassengerProfile(VALID_ID, dto);

        assertThat(updated).isNotNull();
        assertThat(updated.getFirstName()).isEqualTo(dto.firstName());
        assertThat(updated.getEmail()).isEqualTo(dto.email());
        assertThat(updated.getPhone()).isEqualTo(dto.phone());
    }

    @Test
    void updatePassengerProfile_shouldThrow() {
        doThrow(new ResourceNotFoundException(PASSENGER_NOT_FOUND.getCode()))
                .when(profileValidationManager).getProfileByIdIfExists(VALID_ID);

        assertThatThrownBy(() ->
                commandPassengerProfileService.updatePassengerProfile(VALID_ID, ProfileUtil.validProfileDto()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(PASSENGER_NOT_FOUND.getCode());
    }

    @Test
    void deletePassengerProfile_shouldCallDelete() {
        doNothing().when(profileValidationManager).checkIfProfileExists(VALID_ID);

        commandPassengerProfileService.deletePassengerProfile(VALID_ID);

        verify(passengerProfileRepo, times(1)).deleteById(VALID_ID);
    }

    @Test
    void deletePassengerProfile_shouldThrow() {
        doThrow(new ResourceNotFoundException(PASSENGER_NOT_FOUND.getCode()))
                .when(profileValidationManager).checkIfProfileExists(VALID_ID);

        assertThatThrownBy(() -> commandPassengerProfileService.deletePassengerProfile(VALID_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(PASSENGER_NOT_FOUND.getCode());
    }

    @Test
    void readPassengerProfiles_returnsDataPackage() {
        ProfileFilterRequest filter = ProfileUtil.validProfileFilterRequest();
        List<PassengerProfile> profiles = List.of(ProfileUtil.validPassengerProfile());
        Page<PassengerProfile> page = new PageImpl<>(profiles);

        Specification<PassengerProfile> spec = mock(Specification.class);

        when(passengerProfileSpecification.filterBy(filter)).thenReturn(spec);
        when(passengerProfileRepo.findAll(eq(spec), any(Pageable.class))).thenReturn(page);
        when(profileMapper.handleEntity(any(PassengerProfile.class)))
                .thenReturn(ProfileUtil.responseProfileDto());

        DataPackageDto result = readPassengerProfileService.readPassengerProfiles(filter);

        assertThat(result).isNotNull();
        assertThat(result.profiles()).hasSize(profiles.size());
        assertThat(result.totalElements()).isEqualTo(profiles.size());
        assertThat(result.pageNumber()).isEqualTo(DEFAULT_PAGE_VALUE);
        assertThat(result.pageSize()).isEqualTo(profiles.size());
        assertThat(result.totalPages()).isEqualTo(1);

        verify(passengerProfileRepo).findAll(eq(spec), any(Pageable.class));
    }
}