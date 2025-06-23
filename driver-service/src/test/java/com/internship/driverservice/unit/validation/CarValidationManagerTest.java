package com.internship.driverservice.unit.validation;

import com.internship.driverservice.entity.Car;
import com.internship.driverservice.repo.CarRepo;
import com.internship.driverservice.utils.exceptions.ExceptionCodes;
import com.internship.driverservice.utils.exceptions.InvalidInputException;
import com.internship.driverservice.utils.exceptions.ResourceNotFoundException;
import com.internship.driverservice.utils.validation.CarValidationManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.internship.driverservice.util.UtilConstants.DEFAULT_CAR_NUMBER;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarValidationManagerTest {

    @Mock
    private CarRepo carRepo;

    @InjectMocks
    private CarValidationManager validationManager;

    @Test
    void checkCarNotNull_throwsExceptionIfNull() {
        assertThatThrownBy(() -> validationManager.checkCarNotNull(null))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(ExceptionCodes.CAR_NOT_FOUND.getCode());
    }

    @Test
    void validateCarNumberUniqueness_throwsExceptionIfExists() {
        when(carRepo.existsByCarNumberIgnoreCase(DEFAULT_CAR_NUMBER)).thenReturn(true);

        assertThatThrownBy(() -> validationManager.validateCarNumberUniqueness(DEFAULT_CAR_NUMBER))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(ExceptionCodes.CAR_NUMBER_ALREADY_EXISTS.getCode());
    }

    @Test
    void getIfExistsById_returnsCarIfPresent() {
        Car car = Car.builder().id(DEFAULT_ID).build();
        when(carRepo.findById(DEFAULT_ID)).thenReturn(Optional.of(car));

        Car result = validationManager.getIfExistsById(DEFAULT_ID);
        assertThat(result).isEqualTo(car);
    }

    @Test
    void getIfExistsById_throwsExceptionIfNotPresent() {
        when(carRepo.findById(DEFAULT_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> validationManager.getIfExistsById(DEFAULT_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(ExceptionCodes.CAR_NOT_FOUND.getCode());
    }
}