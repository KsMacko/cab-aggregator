package com.internship.driverservice.unit.validation;

import com.internship.driverservice.entity.Notification;
import com.internship.driverservice.enums.notification.NotificationActivity;
import com.internship.driverservice.enums.notification.NotificationType;
import com.internship.driverservice.repo.NotificationRepo;
import com.internship.driverservice.utils.exceptions.InvalidInputException;
import com.internship.driverservice.utils.exceptions.ResourceNotFoundException;
import com.internship.driverservice.utils.validation.NotificationValidationManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.internship.driverservice.util.UtilConstants.DEFAULT_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationValidationManagerTest {

    @Mock
    private NotificationRepo notificationRepo;

    @InjectMocks
    private NotificationValidationManager validationManager;

    @Test
    void checkNotificationAccordance_returnsNotificationIfCorrectTypeAndActive() {
        Notification notification = mock(Notification.class);
        when(notification.getType()).thenReturn(NotificationType.RIDE_CREATION);
        when(notification.getActivity()).thenReturn(NotificationActivity.ACTIVE);

        when(notificationRepo.findById(DEFAULT_ID)).thenReturn(Optional.of(notification));

        Notification result = validationManager.checkNotificationAccordance(DEFAULT_ID, NotificationType.RIDE_CREATION);
        assertThat(result).isEqualTo(notification);
    }

    @Test
    void checkNotificationAccordance_throwsExceptionIfWrongType() {
        Notification notification = mock(Notification.class);
        when(notification.getType()).thenReturn(NotificationType.CASH_CONFIRMATION);

        when(notificationRepo.findById(DEFAULT_ID)).thenReturn(Optional.of(notification));

        assertThatThrownBy(() -> validationManager.checkNotificationAccordance(DEFAULT_ID, NotificationType.RIDE_CREATION))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining("notification.doNotAccord");
    }

    @Test
    void checkNotificationAccordance_throwsExceptionIfNonActive() {
        Notification notification = mock(Notification.class);
        when(notification.getType()).thenReturn(NotificationType.RIDE_CREATION);
        when(notification.getActivity()).thenReturn(NotificationActivity.NON_ACTIVE);

        when(notificationRepo.findById(DEFAULT_ID)).thenReturn(Optional.of(notification));

        assertThatThrownBy(() -> validationManager.checkNotificationAccordance(DEFAULT_ID, NotificationType.RIDE_CREATION))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining("notification.nonActive");
    }

    @Test
    void getNotificationByIdIfExists_throwsExceptionIfNotFound() {
        when(notificationRepo.findById(DEFAULT_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> validationManager.getNotificationByIdIfExists(DEFAULT_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("notification.notFound");
    }
}