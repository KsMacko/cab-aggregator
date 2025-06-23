package com.internship.rideservice.integration;

import com.internship.rideservice.config.JsonFileReader;
import com.internship.rideservice.entity.Ride;
import com.internship.rideservice.repo.RideRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.internship.rideservice.config.JsonFiles.BASE_RIDES;
import static com.internship.rideservice.config.JsonFiles.BASE_URL;
import static com.internship.rideservice.config.JsonFiles.invalidRideRequest;
import static com.internship.rideservice.config.JsonFiles.validRideFilterRequest;
import static com.internship.rideservice.config.JsonFiles.validRideRequest;
import static com.internship.rideservice.utils.RideUtil.rideEntity;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RideControllerIT extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RideRepo rideRepo;


    @Test
    @DisplayName("Create ride with valid data - should return 201 Created and Location header")
    void createRide_shouldReturnCreated_withLocationHeader() throws Exception {
        mockMvc.perform(post(BASE_RIDES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRideRequest))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, containsString(BASE_RIDES + "/")))
                .andReturn();
    }

    @Test
    @DisplayName("Try to create ride with invalid data - should return validation errors")
    void createRide_withInvalidData_shouldReturnValidationError() throws Exception {
        mockMvc.perform(post(BASE_RIDES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRideRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    @DisplayName("Get ride by ID after creation - should return 200 OK")
    void getRideById_afterCreation_shouldReturnOk() throws Exception {
        Ride ride = createdRide();
        mockMvc.perform(get(BASE_RIDES + "/{id}", ride.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ride.getId()));
    }

    @Test
    @DisplayName("Check participants in ride - should return 200 OK")
    void checkParticipants_shouldReturnOk_whenExists() throws Exception {
        mockMvc.perform(get(BASE_RIDES + "/{id}/participants", createdRide().getId()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete ride by ID - should return 204 No Content")
    void deleteRide_byId_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete(BASE_RIDES)
                        .param("id", createdRide().getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Get all rides with filter - should return paginated list")
    void getAllRides_withValidFilter_shouldReturnList() throws Exception {
        mockMvc.perform(get(BASE_RIDES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .params(JsonFileReader.parseJsonToQueryParams(validRideFilterRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ridesDto").isArray())
                .andExpect(jsonPath("$.totalElements").value(1));
    }
    private Ride createdRide(){
        return rideRepo.save(rideEntity());
    }
}