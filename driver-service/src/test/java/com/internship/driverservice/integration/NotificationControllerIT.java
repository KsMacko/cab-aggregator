package com.internship.driverservice.integration;

import com.internship.driverservice.entity.DriverProfile;
import com.internship.driverservice.entity.PaymentByCashConfirmation;
import com.internship.driverservice.entity.RideCreationNotification;
import com.internship.driverservice.repo.DriverProfileRepo;
import com.internship.driverservice.repo.NotificationRepo;
import com.internship.driverservice.util.NotificationUtil;
import com.internship.driverservice.util.ProfileUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.internship.driverservice.util.ProfileUtil.driverProfile;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_ID;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_STR_ID;
import static com.internship.driverservice.util.UtilConstants.NOTIFICATION_BASE_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class NotificationControllerIT extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NotificationRepo notificationRepo;
    @Autowired
    private DriverProfileRepo driverProfileRepo;

    @Test
    @DisplayName("Confirm Cash Payment - should return updated data")
    void confirmCashPayment_shouldReturnUpdatedData_whenValidRequest() throws Exception {
        DriverProfile createdProfile = driverProfileRepo.save(driverProfile());
        PaymentByCashConfirmation paymentConfirmationDetails = NotificationUtil.paymentByCashConfirmation();
        paymentConfirmationDetails.getNotification().setDriverProfile(createdProfile);
        paymentConfirmationDetails.getNotification().setPaymentByCashConfirmation(paymentConfirmationDetails);
        Long paymentNotificationId = notificationRepo.save(paymentConfirmationDetails.getNotification()).getId();
        mockMvc.perform(post(NOTIFICATION_BASE_URL + "/confirm-payment-notification/{id}/status", paymentNotificationId)
                        .param("status", "ACCEPTED"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Confirm Cash Payment - invalid status should return 400")
    void confirmCashPayment_withInvalidStatus_shouldReturnValidationError() throws Exception {
        mockMvc.perform(post(NOTIFICATION_BASE_URL + "/confirm-payment-notification/{id}/status", -DEFAULT_ID)
                        .param("status", "invalid_status"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @Test
    @DisplayName("Update Ride Creation Notification - should return updated data")
    void updateRideCreationNotificationStatus_shouldReturnUpdatedData_whenValidRequest() throws Exception {
        DriverProfile createdProfile = driverProfileRepo.save(driverProfile());
        RideCreationNotification rideCreationDetails = NotificationUtil.rideCreationNotification();
        rideCreationDetails.getNotification().setDriverProfile(createdProfile);
        rideCreationDetails.getNotification().setRideCreationNotification(rideCreationDetails);
        Long notificationId = notificationRepo.save(rideCreationDetails.getNotification()).getId();
        mockMvc.perform(post(NOTIFICATION_BASE_URL + "/ride-creation-notification/{id}/status", notificationId)
                        .param("status", "ACCEPTED"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Update Ride Creation Notification - invalid ID should return 400")
    void updateRideCreationNotificationStatus_withInvalidId_shouldReturnValidationError() throws Exception {
        mockMvc.perform(post(NOTIFICATION_BASE_URL + "/ride-creation-notification/{id}/status", -DEFAULT_ID)
                        .param("status", "ACCEPTED"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @Test
    @DisplayName("Update Ride Status - empty rideId should return 400")
    void updateCurrentRideStatus_withEmptyRideId_shouldReturnValidationError() throws Exception {
        mockMvc.perform(post(NOTIFICATION_BASE_URL + "/current-ride/{rideId}/status", "invalid")
                        .param("status", "invalid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }
}
