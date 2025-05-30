package com.internship.passengerservice.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.internship.passengerservice.config.JsonFiles.BASE_PASSENGERS;
import static com.internship.passengerservice.config.JsonFiles.invalidPassengerRequest;
import static com.internship.passengerservice.config.JsonFiles.validPassengerFilterRequest;
import static com.internship.passengerservice.config.JsonFiles.validPassengerRequest;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PassengerControllerIT extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

//    @Autowired
//    private PassengerProfileRepo passengerProfileRepo;
//
//    @Autowired
//    private RateRepo rateRepo;

    private static Long createdPassengerId;

    @Test
    @Order(1)
    @DisplayName("Create passenger profile - should return 201 and location")
    void createPassengerProfile_shouldReturnCreated() throws Exception {
        MvcResult result = mockMvc.perform(post(BASE_PASSENGERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validPassengerRequest))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, containsString(BASE_PASSENGERS + "/")))
                .andReturn();

        String location = result.getResponse().getHeader(HttpHeaders.LOCATION);
        createdPassengerId = Long.valueOf(location.replace("/api/v1/passengers/", ""));
    }

    @Test
    @Order(2)
    @DisplayName("Update passenger profile - should return 200 and updated data")
    void updatePassengerProfile_shouldReturnOk_whenValidData() throws Exception {
        mockMvc.perform(put(BASE_PASSENGERS + "/{id}", createdPassengerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPassengerRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    @DisplayName("Get passengers with filter - should return list of passengers")
    void getPassengers_withValidFilter_shouldReturnList() throws Exception {
        mockMvc.perform(get(BASE_PASSENGERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validPassengerFilterRequest))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    @DisplayName("Delete passenger by ID - should return 204 No Content")
    void deletePassengerById_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete(BASE_PASSENGERS + "/{id}", createdPassengerId))
                .andExpect(status().isNoContent());
    }
}