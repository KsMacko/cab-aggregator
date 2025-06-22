package com.internship.rideservice.integration;

import com.internship.rideservice.entity.Fare;
import com.internship.rideservice.repo.FareRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.internship.rideservice.config.JsonFiles.BASE_FARES;
import static com.internship.rideservice.config.JsonFiles.invalidFareRequest;
import static com.internship.rideservice.config.JsonFiles.validFareRequest;
import static com.internship.rideservice.utils.FareUtil.fareEntity;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FareControllerIT extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FareRepo fareRepo;

    @Test
    @DisplayName("Create fare with valid data - should return 201 and location")
    void createFare_shouldReturnCreated_withLocationHeader() throws Exception {
        mockMvc.perform(post(BASE_FARES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validFareRequest))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, containsString(BASE_FARES + "/")))
                .andReturn();
    }

    @Test
    @DisplayName("Try to create fare with invalid data - should return validation errors")
    void createFare_withInvalidData_shouldReturnValidationError() throws Exception {
        mockMvc.perform(post(BASE_FARES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidFareRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    @DisplayName("Get fare by type after creation - should return 200")
    void getFareByType_shouldReturnOk() throws Exception {
        Fare fare = createFare();
        mockMvc.perform(get(BASE_FARES + "/{type}", fare.getType()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value(fare.getType().toString()));
    }

    @Test
    @DisplayName("Get all fares - should return list of fares")
    void getAllFares_shouldReturnList() throws Exception {
        mockMvc.perform(get(BASE_FARES))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fares").isArray())
                .andExpect(jsonPath("$.totalCount").value(1));
    }

    @Test
    @DisplayName("Delete fare by type - should return 204")
    void deleteFare_byType_shouldReturnNoContent() throws Exception {
       Fare fare = createFare();
        mockMvc.perform(delete(BASE_FARES + "/{type}", fare.getType()))
                .andExpect(status().isNoContent());
    }

    private Fare createFare(){
        return fareRepo.save(fareEntity());
    }
}