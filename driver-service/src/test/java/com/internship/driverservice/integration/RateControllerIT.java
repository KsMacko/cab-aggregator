package com.internship.driverservice.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.internship.driverservice.dto.response.ResponseRateDto;
import com.internship.driverservice.entity.DriverProfile;
import com.internship.driverservice.entity.Rate;
import com.internship.driverservice.repo.DriverProfileRepo;
import com.internship.driverservice.repo.RateRepo;
import com.internship.driverservice.util.ProfileUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static com.internship.driverservice.config.JsonFiles.validJsonRateRequest;
import static com.internship.driverservice.util.ProfileUtil.driverProfile;
import static com.internship.driverservice.util.RateUtil.rateEntity;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_ID;
import static com.internship.driverservice.util.UtilConstants.RATE_BASE_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class RateControllerIT extends BaseTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DriverProfileRepo driverProfileRepo;

    @Autowired
    private RateRepo rateRepo;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);;

    @Test
    @DisplayName("Set rate to driver - should return 201 Created")
    void setRateToDriver_shouldReturnCreated() throws Exception {
        mockMvc.perform(post(RATE_BASE_URL+"/author/passenger")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRateJson()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete rate from driver - should return 204 No Content")
    void deleteRateFromDriver_shouldReturnNoContent() throws Exception {
        Rate rate = createRate();
        mockMvc.perform(delete(RATE_BASE_URL+"/{rateId}/author/passenger/{passengerId}",
                        rate.getId(),
                        rate.getAuthorId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Set rate to passenger - should return 200 Created")
    void setRateToPassenger_shouldReturnCreated() throws Exception {
        mockMvc.perform(post(RATE_BASE_URL+"/author/driver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonRateRequest))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete rate from passenger - should return 204 No Content")
    void deleteRateFromPassenger_shouldReturnNoContent() throws Exception {
        Rate rate = createRate();
        mockMvc.perform(delete(RATE_BASE_URL+"/{rateId}/author/driver/{driverId}",
                        rate.getId(), rate.getAuthorId()))
                .andExpect(status().isNoContent());
    }
    private String createRateJson() throws JsonProcessingException {
        DriverProfile profile = driverProfile();
        Long createdProfileId = driverProfileRepo.save(profile).getProfileId();
        JsonNode jsonNode = objectMapper.readTree(validJsonRateRequest);
        ObjectNode objectNode = (ObjectNode) jsonNode;
        objectNode.put("recipientId", createdProfileId);
        return objectMapper.writeValueAsString(objectNode);
    }
    private Rate createRate(){
        DriverProfile profile = driverProfile();
        Rate rate = rateEntity();
        rate.setDriver(driverProfileRepo.save(profile));
        return rateRepo.save(rate);
    }
}