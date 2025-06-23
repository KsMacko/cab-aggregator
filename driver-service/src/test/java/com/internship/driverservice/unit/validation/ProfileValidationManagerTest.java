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

import static com.internship.driverservice.util.UtilConstants.DEFAULT_ID;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_STR;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_STR_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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

    @Test
    void checkIfProfileExists_throwsExceptionIfEmpty() {
        when(driverProfileRepo.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> validationManager.checkIfProfileExists(DEFAULT_ID))
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
        when(driverProfileRepo.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> validationManager.getDriverProfile(DEFAULT_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(ExceptionCodes.DRIVER_NOT_FOUND.getCode());
    }

    @Test
    void checkIfPhoneUnique_throwsExceptionIfExists() {
        when(driverProfileRepo.existsByPhone(anyString())).thenReturn(true);

        assertThatThrownBy(() -> validationManager.checkIfPhoneUnique(DEFAULT_STR))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(ExceptionCodes.DRIVER_PHONE_UNIQUE.getCode());
    }

    @Test
    void findDriverByAcceptedRide_throwsExceptionIfNotificationNotFound() {
        when(notificationRepo.findByRideIdAndStatusAndType(
                anyString(), any(NotificationStatus.class), any(NotificationType.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> validationManager.findDriverByAcceptedRide(DEFAULT_STR_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(ExceptionCodes.DRIVER_FOR_RIDE_NOT_FOUND.getCode());
    }

    @Test
    void findDriverByAcceptedRide_returnsDriverProfile() {
        Notification notification = mock(Notification.class);
        DriverProfile driverProfile = mock(DriverProfile.class);

        when(notification.getDriverProfile()).thenReturn(driverProfile);
        when(notificationRepo.findByRideIdAndStatusAndType(
                anyString(), any(NotificationStatus.class), any(NotificationType.class)))
                .thenReturn(Optional.of(notification));

        DriverProfile result = validationManager.findDriverByAcceptedRide(DEFAULT_STR_ID);
        assertThat(result).isEqualTo(driverProfile);
    }
}
