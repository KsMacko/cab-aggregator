package com.internship.driverservice.unit.service.command;

import com.internship.driverservice.dto.mapper.ProfileMapper;
import com.internship.driverservice.dto.request.RequestProfileDto;
import com.internship.driverservice.dto.response.ResponseProfileDto;
import com.internship.driverservice.entity.DriverProfile;
import com.internship.driverservice.repo.DriverProfileRepo;
import com.internship.driverservice.service.command.CommandDriverProfileService;
import com.internship.driverservice.service.communication.FinanceFeignClient;
import com.internship.driverservice.utils.validation.ProfileValidationManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.internship.driverservice.util.ProfileUtil.driverProfile;
import static com.internship.driverservice.util.ProfileUtil.requestProfileDto;
import static com.internship.driverservice.util.ProfileUtil.responseProfileDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CommandDriverProfileService unit tests")
class CommandDriverProfileServiceTest {

    @Mock
    private DriverProfileRepo driverProfileRepo;

    @Mock
    private FinanceFeignClient financeFeignClient;

    @Mock
    private ProfileMapper profileMapper;

    @Mock
    private ProfileValidationManager profileValidationManager;

    @InjectMocks
    private CommandDriverProfileService commandDriverProfileService;

    @Test
    @DisplayName("createProfile creates and returns response")
    void createProfile() {
        RequestProfileDto dto = requestProfileDto();
        DriverProfile entity = driverProfile();
        ResponseProfileDto response = responseProfileDto();

        when(profileMapper.handleDto(dto)).thenReturn(entity);
        when(profileMapper.handleEntity(entity)).thenReturn(response);
        when(driverProfileRepo.save(entity)).thenReturn(entity);

        ResponseProfileDto result = commandDriverProfileService.createProfile(dto);

        verify(profileValidationManager).checkIfPhoneUnique(dto.phone());
        verify(financeFeignClient).createWallet(entity.getProfileId());
        verify(driverProfileRepo).save(entity);
        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("updateDriverProfile updates and returns updated profile")
    void updateDriverProfile() {
        Long profileId = 1L;
        RequestProfileDto dto = requestProfileDto();
        DriverProfile existingProfile = mock(DriverProfile.class);
        ResponseProfileDto response = responseProfileDto();

        when(profileMapper.handleEntity(existingProfile)).thenReturn(response);
        when(driverProfileRepo.save(existingProfile)).thenReturn(existingProfile);
        when(profileValidationManager.getDriverProfile(profileId)).thenReturn(existingProfile);

        ResponseProfileDto result = commandDriverProfileService.updateDriverProfile(profileId, dto);

        verify(profileMapper).updateProfileFromDto(dto, existingProfile);
        verify(driverProfileRepo).save(existingProfile);
        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("deleteDriverProfile checks existence, deletes wallet and profile")
    void deleteDriverProfile_deletesSuccessfully() {
        Long profileId = 1L;

        commandDriverProfileService.deleteDriverProfile(profileId);

        verify(profileValidationManager).checkIfProfileExists(profileId);
        verify(financeFeignClient).deleteWallet(profileId);
        verify(driverProfileRepo).deleteById(profileId);
    }
}