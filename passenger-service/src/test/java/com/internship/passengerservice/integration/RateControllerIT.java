package com.internship.passengerservice.integration;

import com.internship.passengerservice.entity.PassengerProfile;
import com.internship.passengerservice.repo.PassengerProfileRepo;
import com.internship.passengerservice.util.ProfileUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.internship.passengerservice.config.JsonFiles.BASE_RATES;
import static com.internship.passengerservice.config.JsonFiles.validRateRequest;
import static com.internship.passengerservice.util.UtilConstants.VALID_ID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RateControllerIT extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PassengerProfileRepo passengerProfileRepo;
    @BeforeAll
    public void setUp(){
        PassengerProfile profile = ProfileUtil.validPassengerProfile();
        profile.setProfileId(null);
        System.out.println("---------------------------------");
        System.out.println(passengerProfileRepo.save(profile).getProfileId());
    }

    @Test
    @Order(1)
    @DisplayName("Set rate to driver - should return 200")
    void setRateToDriver_shouldReturnOk() throws Exception {
        mockMvc.perform(post(BASE_RATES + "/author/passenger")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRateRequest))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @DisplayName("Set rate to passenger - should return 200")
    void setRateToPassenger_shouldReturnOk() throws Exception {
        mockMvc.perform(post(BASE_RATES + "/author/driver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRateRequest))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    @DisplayName("Delete rate from passenger - should return 204 No Content")
    void deleteRateFromPassenger_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete(BASE_RATES + "/{rateId}/author/passenger/{passengerId}",
                        VALID_ID, VALID_ID+1))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(4)
    @DisplayName("Delete rate from driver - should return 204 No Content")
    void deleteRateFromDriver_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete(BASE_RATES + "/{rateId}/author/driver/{driverId}",
                        VALID_ID, VALID_ID+1))
                .andExpect(status().isNoContent());
    }
}