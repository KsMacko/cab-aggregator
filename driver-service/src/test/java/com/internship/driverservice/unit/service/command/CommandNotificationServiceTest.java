package com.internship.driverservice.unit.service.command;

import com.internship.commonevents.event.CashConfirmationRequest;
import com.internship.commonevents.event.ChangeRideStatusEvent;
import com.internship.commonevents.event.ConfirmedPaymentRequest;
import com.internship.commonevents.event.RideNotificationEvent;
import com.internship.driverservice.entity.DriverProfile;
import com.internship.driverservice.entity.Notification;
import com.internship.driverservice.entity.PaymentByCashConfirmation;
import com.internship.driverservice.entity.RideCreationNotification;
import com.internship.driverservice.enums.DriverStatus;
import com.internship.driverservice.enums.FareType;
import com.internship.driverservice.enums.notification.NotificationActivity;
import com.internship.driverservice.enums.notification.NotificationStatus;
import com.internship.driverservice.enums.notification.NotificationType;
import com.internship.driverservice.repo.DriverProfileRepo;
import com.internship.driverservice.repo.NotificationRepo;
import com.internship.driverservice.service.communication.ArtemisProducer;
import com.internship.driverservice.utils.validation.NotificationValidationManager;
import com.internship.driverservice.utils.validation.ProfileValidationManager;
import com.internship.driverservice.service.command.CommandNotificationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static com.internship.driverservice.util.UtilConstants.DEFAULT_ID;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_NOTIFICATION_STATUS;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_STR_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CommandNotificationService unit tests")
class CommandNotificationServiceTest {

    @Mock
    private NotificationRepo notificationRepo;

    @Mock
    private DriverProfileRepo driverProfileRepo;

    @Mock
    private ArtemisProducer artemisProducer;

    @Mock
    private ProfileValidationManager profileValidationManager;

    @Mock
    private NotificationValidationManager notificationValidationManager;

    @InjectMocks
    private CommandNotificationService commandNotificationService;

    @Mock
    private Notification notification;

    @Mock
    private RideCreationNotification rideCreationNotification;
    @Mock
    private PaymentByCashConfirmation paymentConfirmation;
    @Mock
    private DriverProfile driverProfile;

    @BeforeEach
    void setUp() {
        driverProfile.setProfileId(DEFAULT_ID);
        notification.setDriverProfile(driverProfile);
        notification.setRideId(DEFAULT_STR_ID);
    }

    @Test
    @DisplayName("updateRideCreatedNotification updates status and activity")
    void updateRideCreatedNotification_updatesStatusAndActivity() {

        when(notificationValidationManager.checkNotificationAccordance(DEFAULT_ID, NotificationType.RIDE_CREATION))
                .thenReturn(notification);
        when(notification.getRideCreationNotification()).thenReturn(rideCreationNotification);
        when(notification.getDriverProfile()).thenReturn(driverProfile);
        when(profileValidationManager.findDriverByAcceptedRide(DEFAULT_STR_ID)).thenReturn(driverProfile);
        when(notification.getRideId()).thenReturn(DEFAULT_STR_ID);

        RideCreationNotification result = commandNotificationService.updateRideCreatedNotification(
                DEFAULT_ID,
                DEFAULT_NOTIFICATION_STATUS);

        verify(notification).setStatus(NotificationStatus.ACCEPTED);
        verify(notification).setActivity(NotificationActivity.NON_ACTIVE);
        verify(notificationRepo).save(notification);
        assertThat(result).isEqualTo(notification.getRideCreationNotification());
    }

    @Test
    @DisplayName("updatePaymentByCashNotification updates status")
    void updatePaymentByCashNotification_updatesStatus() {
        Long notificationId = 1L;
        String status = "ACCEPTED";

        when(notificationValidationManager.checkNotificationAccordance(notificationId, NotificationType.CASH_CONFIRMATION))
                .thenReturn(notification);

        when(notification.getPaymentByCashConfirmation()).thenReturn(paymentConfirmation);
        when(notification.getDriverProfile()).thenReturn(driverProfile);
        PaymentByCashConfirmation result = commandNotificationService.updatePaymentByCashNotification(notificationId, status);

        verify(notification).setStatus(NotificationStatus.ACCEPTED);
        verify(notification).setActivity(NotificationActivity.NON_ACTIVE);
        verify(driverProfile).setDriverStatus(DriverStatus.FREE);
        verify(artemisProducer).sendCashPaymentConfirmation(any(ConfirmedPaymentRequest.class));
        assertThat(result).isEqualTo(notification.getPaymentByCashConfirmation());
    }

    @Test
    @DisplayName("notifyAboutRideCreation creates notifications for eligible drivers")
    void notifyAboutRideCreation_createsNotifications() {
        RideNotificationEvent event = new RideNotificationEvent("ride123", "ECONOMY", "pickup", List.of("dropoff1", "dropoff2"));
        DriverProfile driver = mock(DriverProfile.class);
        when(driver.getProfileId()).thenReturn(1L);
        when(driverProfileRepo.findByDriverStatusAndFareType(DriverStatus.FREE, FareType.ECONOMY)).thenReturn(List.of(driver));

        commandNotificationService.notifyAboutRideCreation(event);

        verify(notificationRepo, times(1)).save(any(Notification.class));
    }

    @Test
    @DisplayName("notifyAboutRidePaymentByCashConfirmation creates and saves cash confirmation")
    void notifyAboutRidePaymentByCashConfirmation_createsAndSaves() {
        CashConfirmationRequest event = new CashConfirmationRequest("ride123", 1L, 1L, new BigDecimal("10.50"));

        commandNotificationService.notifyAboutRidePaymentByCashConfirmation(event);

        verify(notificationRepo).save(any(Notification.class));
    }

    @Test
    @DisplayName("updateRideStatus builds and sends ChangeRideStatusEvent")
    void updateRideStatus_sendsEvent() {
        String rideId = "ride123";
        String rideStatus = "IN_PROGRESS";

        when(profileValidationManager.findDriverByAcceptedRide(rideId)).thenReturn(driverProfile);

        ChangeRideStatusEvent event = commandNotificationService.updateRideStatus(rideId, rideStatus);

        assertThat(event.status()).isEqualTo(rideStatus);
        assertThat(event.rideId()).isEqualTo(rideId);
        verify(artemisProducer).sendRideStatusUpdate(event);
    }
}