package com.internship.passengerservice.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.internship.passengerservice.entity.PassengerProfile;
import com.internship.passengerservice.entity.Rate;
import com.internship.passengerservice.repo.PassengerProfileRepo;
import com.internship.passengerservice.repo.RateRepo;
import com.internship.passengerservice.util.ProfileUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.internship.passengerservice.config.JsonFiles.BASE_RATES;
import static com.internship.passengerservice.config.JsonFiles.validRateRequest;
import static com.internship.passengerservice.util.ProfileUtil.validPassengerProfile;
import static com.internship.passengerservice.util.RateUtil.validRateEntity;
import static com.internship.passengerservice.util.UtilConstants.VALID_ID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class RateControllerIT extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PassengerProfileRepo passengerProfileRepo;

    @Autowired
    private RateRepo rateRepo;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    @DisplayName("Set rate to driver - should return 200")
    void setRateToDriver_shouldReturnOk() throws Exception {
        mockMvc.perform(post(BASE_RATES + "/author/passenger")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRateRequest))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Set rate to passenger - should return 200")
    void setRateToPassenger_shouldReturnOk() throws Exception {
        Long createdProfileId = passengerProfileRepo.save(validPassengerProfile()).getProfileId();
        JsonNode jsonNode = objectMapper.readTree(validRateRequest);
        ObjectNode objectNode = (ObjectNode) jsonNode;
        objectNode.put("recipientId", createdProfileId);
        String modifiedJson =  objectMapper.writeValueAsString(objectNode);
        mockMvc.perform(post(BASE_RATES + "/author/driver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(modifiedJson))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete rate from passenger - should return 204 No Content")
    void deleteRateFromPassenger_shouldReturnNoContent() throws Exception {
        Rate rate = createRate();
        mockMvc.perform(delete(BASE_RATES + "/{rateId}/author/passenger/{passengerId}",
                        rate.getId(), rate.getAuthorId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Delete rate from driver - should return 204 No Content")
    void deleteRateFromDriver_shouldReturnNoContent() throws Exception {
        Rate rate = createRate();
        mockMvc.perform(delete(BASE_RATES + "/{rateId}/author/driver/{driverId}",
                        rate.getId(), rate.getAuthorId()))
                .andExpect(status().isNoContent());
    }
    private Rate createRate(){
        Rate rate = validRateEntity();
        rate.setPassenger(passengerProfileRepo.save(validPassengerProfile()));
        return rateRepo.save(rate);
    }
}