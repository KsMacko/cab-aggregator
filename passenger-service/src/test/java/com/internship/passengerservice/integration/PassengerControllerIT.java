package com.internship.passengerservice.integration;

import com.internship.passengerservice.config.JsonFileReader;
import com.internship.passengerservice.entity.PassengerProfile;
import com.internship.passengerservice.repo.PassengerProfileRepo;
import com.internship.passengerservice.repo.RateRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static com.internship.passengerservice.config.JsonFiles.BASE_PASSENGERS;
import static com.internship.passengerservice.config.JsonFiles.BASE_URL;
import static com.internship.passengerservice.config.JsonFiles.invalidPassengerRequest;
import static com.internship.passengerservice.config.JsonFiles.validPassengerFilterRequest;
import static com.internship.passengerservice.config.JsonFiles.validPassengerRequest;
import static com.internship.passengerservice.util.ProfileUtil.validPassengerProfile;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class PassengerControllerIT extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PassengerProfileRepo passengerProfileRepo;

    @Test
    @DisplayName("Create passenger profile - should return 201 and location")
    void createPassengerProfile_shouldReturnCreated() throws Exception {
        mockMvc.perform(post(BASE_PASSENGERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validPassengerRequest))
                .andExpect(status().isCreated())
                .andReturn();

    }

    @Test
    @DisplayName("Update passenger profile - should return 200 and updated data")
    void updatePassengerProfile_shouldReturnOk_whenValidData() throws Exception {
        PassengerProfile passengerProfile = createPassenger();
        mockMvc.perform(put(BASE_PASSENGERS + "/{id}", passengerProfile.getProfileId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validPassengerRequest))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get passengers with filter - should return list of passengers")
    void getPassengers_withValidFilter_shouldReturnList() throws Exception {
        mockMvc.perform(get(BASE_PASSENGERS).params(JsonFileReader.parseJsonToQueryParams(validPassengerFilterRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profiles").isArray());
    }

    @Test
    @DisplayName("Delete passenger by ID - should return 204 No Content")
    void deletePassengerById_shouldReturnNoContent() throws Exception {
        PassengerProfile passengerProfile = createPassenger();
        mockMvc.perform(delete(BASE_PASSENGERS + "/{id}", passengerProfile.getProfileId()))
                .andExpect(status().isNoContent());
    }
    private PassengerProfile createPassenger(){
        return passengerProfileRepo.save(validPassengerProfile());
    }
}