package com.internship.driverservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.driverservice.dto.request.RequestProfileDto;
import com.internship.driverservice.repo.DriverProfileRepo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.internship.driverservice.config.JsonFiles.invalidJsonProfileRequest;
import static com.internship.driverservice.config.JsonFiles.validJsonProfileFilterRequest;
import static com.internship.driverservice.config.JsonFiles.validJsonProfileForUpdateRequest;
import static com.internship.driverservice.config.JsonFiles.validJsonProfileRequest;
import static com.internship.driverservice.util.UtilConstants.BASE_URL;
import static com.internship.driverservice.util.UtilConstants.DRIVER_BASE_URL;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProfileControllerIT extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DriverProfileRepo driverProfileRepo;

    private static Long createdProfileId;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Order(1)
    @DisplayName("Create driver profile - should return 201 Created and Location header with ID")
    void createDriverProfile_shouldReturnCreated_withLocationHeader() throws Exception {
        MvcResult result = mockMvc.perform(post(DRIVER_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonProfileRequest))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, containsString(DRIVER_BASE_URL + "/")))
                .andReturn();

        String location = result.getResponse().getHeader(HttpHeaders.LOCATION);
        createdProfileId = Long.valueOf(location.replace(BASE_URL+DRIVER_BASE_URL + "/", ""));
    }

    @Test
    @Order(2)
    @DisplayName("Get driver by ID - should return 200 OK and profile data")
    void findById_shouldReturnProfile_whenExists() throws Exception {
        RequestProfileDto profileDto = objectMapper.readValue(validJsonProfileRequest, RequestProfileDto.class);
        mockMvc.perform(get(DRIVER_BASE_URL + "/{id}", createdProfileId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profileId").value(createdProfileId.intValue()))
                .andExpect(jsonPath("$.firstName").value(profileDto.firstName()))
                .andExpect(jsonPath("$.lastName").value(profileDto.lastName()));
    }

    @Test
    @Order(3)
    @DisplayName("Find all drivers with valid filter - should return list of profiles")
    void findAllDrivers_withValidFilter_shouldReturnListOfProfiles() throws Exception {
        mockMvc.perform(get(DRIVER_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonProfileFilterRequest))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    @DisplayName("Update driver profile - should return updated data")
    void updateProfile_shouldReturnUpdatedData_whenValidRequest() throws Exception {

        RequestProfileDto expected = objectMapper.readValue(validJsonProfileForUpdateRequest, RequestProfileDto.class);

        mockMvc.perform(put(DRIVER_BASE_URL + "/{id}", createdProfileId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonProfileForUpdateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profileId").value(createdProfileId))
                .andExpect(jsonPath("$.firstName").value(expected.firstName()))
                .andExpect(jsonPath("$.lastName").value(expected.lastName()));
    }

    @Test
    @Order(5)
    @DisplayName("Delete driver profile - should return 204 No Content")
    void deleteProfile_shouldReturnNoContent_whenValidId() throws Exception {
        mockMvc.perform(delete(DRIVER_BASE_URL + "/{id}", createdProfileId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Create profile with invalid data - should return validation error")
    void createProfile_shouldReturnValidationError_whenInvalidRequest() throws Exception {
        mockMvc.perform(post(DRIVER_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonProfileRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }
    @AfterAll
    void tearDown() {
        driverProfileRepo.deleteAll();
    }
}

