package com.internship.driverservice.unit.validation;

import com.internship.driverservice.entity.DriverProfile;
import com.internship.driverservice.entity.Notification;
import com.internship.driverservice.enums.notification.NotificationStatus;
import com.internship.driverservice.enums.notification.NotificationType;
import com.internship.driverservice.repo.DriverProfileRepo;
import com.internship.driverservice.repo.NotificationRepo;
import com.internship.driverservice.utils.exceptions.ExceptionCodes;
import com.internship.driverservice.utils.exceptions.InvalidInputException;
import com.internship.driverservice.utils.exceptions.ResourceNotFoundException;
import com.internship.driverservice.utils.validation.ProfileValidationManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileValidationManagerTest {

    @Mock
    private DriverProfileRepo driverProfileRepo;

    @Mock
    private NotificationRepo notificationRepo;

    @InjectMocks
    private ProfileValidationManager validationManager;

    private static final Long PROFILE_ID = 1L;
    private static final String RIDE_ID = "ride123";

    @Test
    void checkIfProfileExists_throwsExceptionIfEmpty() {
        when(driverProfileRepo.findById(PROFILE_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> validationManager.checkIfProfileExists(PROFILE_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(ExceptionCodes.DRIVER_NOT_FOUND.getCode());
    }

    @Test
    void checkIfProfileIdNotNull_throwsExceptionIfNull() {
        assertThatThrownBy(() -> validationManager.checkIfProfileIdNotNull(null))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(ExceptionCodes.DRIVER_ID_NOT_NULL.getCode());
    }

    @Test
    void getDriverProfile_throwsExceptionIfNotPresent() {
        when(driverProfileRepo.findById(PROFILE_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> validationManager.getDriverProfile(PROFILE_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(ExceptionCodes.DRIVER_NOT_FOUND.getCode());
    }

    @Test
    void checkIfPhoneUnique_throwsExceptionIfExists() {
        when(driverProfileRepo.existsByPhone("123")).thenReturn(true);

        assertThatThrownBy(() -> validationManager.checkIfPhoneUnique("123"))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(ExceptionCodes.DRIVER_PHONE_UNIQUE.getCode());
    }

    @Test
    void findDriverByAcceptedRide_throwsExceptionIfNotificationNotFound() {
        when(notificationRepo.findByRideIdAndStatusAndType(
                RIDE_ID, NotificationStatus.ACCEPTED, NotificationType.RIDE_CREATION))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> validationManager.findDriverByAcceptedRide(RIDE_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(ExceptionCodes.DRIVER_FOR_RIDE_NOT_FOUND.getCode());
    }

    @Test
    void findDriverByAcceptedRide_returnsDriverProfile() {
        Notification notification = mock(Notification.class);
        DriverProfile driverProfile = mock(DriverProfile.class);

        when(notification.getDriverProfile()).thenReturn(driverProfile);
        when(notificationRepo.findByRideIdAndStatusAndType(
                RIDE_ID, NotificationStatus.ACCEPTED, NotificationType.RIDE_CREATION))
                .thenReturn(Optional.of(notification));

        DriverProfile result = validationManager.findDriverByAcceptedRide(RIDE_ID);
        assertThat(result).isEqualTo(driverProfile);
    }
}
