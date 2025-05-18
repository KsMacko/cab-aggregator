package com.internship.rideservice.unit.validation;

import com.internship.rideservice.entity.Fare;
import com.internship.rideservice.repo.FareRepo;
import com.internship.rideservice.util.exceptions.InvalidInputException;
import com.internship.rideservice.util.exceptions.ResourceNotFoundException;
import com.internship.rideservice.util.validators.FareValidationManager;
import com.internship.rideservice.utils.FareUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.internship.rideservice.util.exceptions.ExceptionCodes.FARE_ALREADY_EXISTS;
import static com.internship.rideservice.util.exceptions.ExceptionCodes.FARE_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FareValidationManagerTest {

    @Mock
    private FareRepo fareRepo;

    @InjectMocks
    private FareValidationManager validationManager;

    @Test
    void getFareIfExists_shouldReturnFare_whenExists() {
        Fare fare = FareUtil.fareEntity();

        when(fareRepo.findFareByType(any())).thenReturn(Optional.of(fare));

        Fare result = validationManager.getFareIfExists(fare.getType());

        assertThat(result).isEqualTo(fare);
    }

    @Test
    void getFareIfExists_shouldThrow_whenNotExists() {
        when(fareRepo.findFareByType(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> validationManager.getFareIfExists(any()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(FARE_NOT_FOUND.getCode());
    }

    @Test
    void checkForDuplicateType_shouldThrow_whenTypeAlreadyExists() {

        when(fareRepo.existsFareByType(any())).thenReturn(true);

        assertThatThrownBy(() -> validationManager.checkForDuplicateType(any()))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(FARE_ALREADY_EXISTS.getCode());
    }

    @Test
    void checkForDuplicateType_shouldDoNothing_whenNoDuplicates() {

        when(fareRepo.existsFareByType(any())).thenReturn(false);

        validationManager.checkForDuplicateType(any());
    }

    @Test
    void checkIfNotExistsByType_shouldThrow_whenFareDoesNotExist() {
        when(fareRepo.existsById(any())).thenReturn(false);

        assertThatThrownBy(() -> validationManager.checkIfNotExistsByType(any()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(FARE_NOT_FOUND.getCode());
    }

    @Test
    void checkIfNotExistsByType_shouldDoNothing_whenExists() {
        when(fareRepo.existsById(any())).thenReturn(true);

        validationManager.checkIfNotExistsByType(any());
    }
}