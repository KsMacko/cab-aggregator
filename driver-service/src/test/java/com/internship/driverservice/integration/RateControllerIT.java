package com.internship.driverservice.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.internship.driverservice.dto.response.ResponseRateDto;
import com.internship.driverservice.entity.DriverProfile;
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

import static com.internship.driverservice.config.JsonFiles.validJsonRateRequest;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_ID;
import static com.internship.driverservice.util.UtilConstants.RATE_BASE_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RateControllerIT extends BaseTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DriverProfileRepo driverProfileRepo;

    @Autowired
    private RateRepo rateRepo;

    private static ResponseRateDto responseRateDto;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);;
    private static String modifiedJson;

    @BeforeAll
    void setUp() throws Exception {
        DriverProfile profile = ProfileUtil.driverProfile();
        profile.setProfileId(null);
        Long createdProfileId = driverProfileRepo.save(profile).getProfileId();
        JsonNode jsonNode = objectMapper.readTree(validJsonRateRequest);
        ObjectNode objectNode = (ObjectNode) jsonNode;
        objectNode.put("recipientId", createdProfileId);
        modifiedJson = objectMapper.writeValueAsString(objectNode);
    }

    @Test
    @DisplayName("Set rate to driver - should return 201 Created")
    @Order(1)
    void setRateToDriver_shouldReturnCreated() throws Exception {
        MvcResult result = mockMvc.perform(post(RATE_BASE_URL+"/author/passenger")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(modifiedJson))
                .andExpect(status().isOk())
                .andReturn();
        String responseJson = result.getResponse().getContentAsString();
        responseRateDto = objectMapper.readValue(responseJson, ResponseRateDto.class);
    }

    @Test
    @DisplayName("Delete rate from driver - should return 204 No Content")
    @Order(2)
    void deleteRateFromDriver_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete(RATE_BASE_URL+"/{rateId}/author/passenger/{passengerId}",
                        responseRateDto.id(),
                        responseRateDto.authorId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Set rate to passenger - should return 200 Created")
    @Order(3)
    void setRateToPassenger_shouldReturnCreated() throws Exception {

        mockMvc.perform(post(RATE_BASE_URL+"/author/driver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonRateRequest))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete rate from passenger - should return 204 No Content")
    @Order(4)
    void deleteRateFromPassenger_shouldReturnNoContent() throws Exception {

        mockMvc.perform(delete(RATE_BASE_URL+"/{rateId}/author/driver/{driverId}", DEFAULT_ID, DEFAULT_ID))
                .andExpect(status().isNoContent());
    }

    @AfterAll
    void tearDown() {
        driverProfileRepo.deleteAll();
        rateRepo.deleteAll();
    }
}