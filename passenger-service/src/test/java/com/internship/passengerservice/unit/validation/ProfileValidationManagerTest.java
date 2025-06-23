package com.internship.passengerservice.unit.validation;

import com.internship.passengerservice.dto.request.RequestProfileDto;
import com.internship.passengerservice.entity.PassengerProfile;
import com.internship.passengerservice.repo.PassengerProfileRepo;
import com.internship.passengerservice.utils.ProfileValidationManager;
import com.internship.passengerservice.utils.exceptions.InvalidInputException;
import com.internship.passengerservice.utils.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.internship.passengerservice.util.ProfileUtil.validPassengerProfile;
import static com.internship.passengerservice.util.ProfileUtil.validProfileDto;
import static com.internship.passengerservice.util.UtilConstants.VALID_EMAIL;
import static com.internship.passengerservice.util.UtilConstants.VALID_ID;
import static com.internship.passengerservice.util.UtilConstants.VALID_PHONE;
import static com.internship.passengerservice.utils.exceptions.ExceptionCodes.EMAIL_ALREADY_EXISTS;
import static com.internship.passengerservice.utils.exceptions.ExceptionCodes.PASSENGER_NOT_FOUND;
import static com.internship.passengerservice.utils.exceptions.ExceptionCodes.PHONE_ALREADY_EXISTS;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileValidationManagerTest {

    @InjectMocks
    private ProfileValidationManager profileValidationManager;

    @Mock
    private PassengerProfileRepo passengerProfileRepo;

    @Test
    void checkIfProfileExists_shouldThrowResourceNotFoundException() {

        when(passengerProfileRepo.existsById(VALID_ID)).thenReturn(false);

        assertThatThrownBy(() -> profileValidationManager.checkIfProfileExists(VALID_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(PASSENGER_NOT_FOUND.getCode());
    }

    @Test
    void checkIfProfileExists_shouldNotThrow() {
        when(passengerProfileRepo.existsById(VALID_ID)).thenReturn(true);

        profileValidationManager.checkIfProfileExists(VALID_ID);
    }

    @Test
    void getProfileByIdIfExists_shouldReturnProfile() {
        PassengerProfile profile = validPassengerProfile();
        when(passengerProfileRepo.findById(VALID_ID)).thenReturn(Optional.of(profile));

        PassengerProfile result = profileValidationManager.getProfileByIdIfExists(VALID_ID);

        assertThat(result).isEqualTo(profile);
    }

    @Test
    void getProfileByIdIfExists_shouldThrowResourceNotFoundException() {
        willReturn(Optional.empty()).given(passengerProfileRepo).findById(VALID_ID);

        assertThatThrownBy(() -> profileValidationManager.getProfileByIdIfExists(VALID_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(PASSENGER_NOT_FOUND.getCode());
    }

    @Test
    void checkEmailUniqueness_shouldThrowInvalidInputException() {
        willReturn(true).given(passengerProfileRepo).existsByEmailIgnoreCase(VALID_EMAIL);

        assertThatThrownBy(() -> profileValidationManager.checkEmailUniqueness(VALID_EMAIL))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(EMAIL_ALREADY_EXISTS.getCode());
    }

    @Test
    void checkPhoneNumberUniqueness_shouldThrowInvalidInputException() {

        willReturn(true).given(passengerProfileRepo).existsByPhoneIgnoreCase(VALID_PHONE);

        assertThatThrownBy(() -> profileValidationManager.checkPhoneNumberUniqueness(VALID_PHONE))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(PHONE_ALREADY_EXISTS.getCode());
    }

    @Test
    void checkProfileToUpdate_shouldCallEmailAndPhoneCheck() {
        RequestProfileDto dto = validProfileDto();
        willReturn(false).given(passengerProfileRepo).existsByEmailIgnoreCase(dto.email());
        willReturn(false).given(passengerProfileRepo).existsByPhoneIgnoreCase(dto.phone());

        profileValidationManager.checkProfileToUpdate(dto);

        then(passengerProfileRepo).should().existsByEmailIgnoreCase(dto.email());
        then(passengerProfileRepo).should().existsByPhoneIgnoreCase(dto.phone());
    }
}