package com.internship.driverservice.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.internship.driverservice.dto.request.RequestCarDto;
import com.internship.driverservice.entity.Car;
import com.internship.driverservice.entity.DriverProfile;
import com.internship.driverservice.repo.CarRepo;
import com.internship.driverservice.repo.DriverProfileRepo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static com.internship.driverservice.config.JsonFiles.invalidJsonCarRequest;
import static com.internship.driverservice.config.JsonFiles.validJsonCarFilterRequest;
import static com.internship.driverservice.config.JsonFiles.validJsonCarRequest;
import static com.internship.driverservice.config.JsonFiles.validJsonProfileRequest;
import static com.internship.driverservice.util.CarUtil.carEntity;
import static com.internship.driverservice.util.ProfileUtil.driverProfile;
import static com.internship.driverservice.util.UtilConstants.BASE_URL;
import static com.internship.driverservice.util.UtilConstants.CAR_BASE_URL;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_ID;
import static com.internship.driverservice.util.UtilConstants.DRIVER_BASE_URL;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class CarControllerIT extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DriverProfileRepo driverProfileRepo;

    @Autowired
    private CarRepo carRepo;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Add new car - should return 201 Created")
    void addNewCar_shouldReturnCreated_whenValidRequest() throws Exception {
        String modifiedJson = createCarJson();
        RequestCarDto expected = objectMapper.readValue(modifiedJson, RequestCarDto.class);
        mockMvc.perform(post(CAR_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(modifiedJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.carNumber").value(expected.carNumber()))
                .andExpect(jsonPath("$.brand").value(expected.brand()))
                .andReturn();
    }

    @Test
    @DisplayName("Get current car by profile ID - should return 200 OK and car data")
    void getCurrentCar_shouldReturnCar_whenProfileHasCurrentCar() throws Exception {
        mockMvc.perform(get(DRIVER_BASE_URL+"/{id}/cars/current", createCar().getDriverProfile().getProfileId()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get cars with valid filter - should return list of cars")
    void getCars_withValidFilter_shouldReturnCarList() throws Exception {
        mockMvc.perform(get(CAR_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonCarFilterRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carsDto").isArray());
    }

    @Test
    @DisplayName("Set current car - should return updated car with isCurrent = true")
    void setCurrentCar_shouldSetCurrent_whenValidId() throws Exception {
        mockMvc.perform(patch(CAR_BASE_URL + "/{id}", createCar().getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isCurrent").value(true));
    }

    @Test
    @DisplayName("Delete car by ID - should return 204 No Content")
    void deleteCar_shouldReturnNoContent_whenValidId() throws Exception {
        Car car = createCar();
        mockMvc.perform(delete(CAR_BASE_URL + "/{id}", car.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Add car with null driverId - should return validation error")
    void addNewCar_shouldReturnValidationError_whenDriverIdIsNull() throws Exception {
        mockMvc.perform(post(CAR_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonCarRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors").isNotEmpty());
    }

    @Test
    @DisplayName("Add car with duplicate number - should return 400 Bad Request")
    void addNewCar_shouldThrowError_whenCarNumberAlreadyExists() throws Exception {
        String modifiedJson = createCarJson();
        mockMvc.perform(post(CAR_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(modifiedJson))
                .andExpect(status().isOk());

        mockMvc.perform(post(CAR_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(modifiedJson))
                .andExpect(status().isBadRequest());
    }
    private String createCarJson() throws JsonProcessingException {
        Long createdProfileId = driverProfileRepo.save(driverProfile()).getProfileId();
        JsonNode jsonNode = objectMapper.readTree(validJsonCarRequest);
        ObjectNode objectNode = (ObjectNode) jsonNode;
        objectNode.put("driverId", createdProfileId);
        return objectMapper.writeValueAsString(objectNode);
    }
    private Car createCar(){
        Car car = carEntity();
        car.setDriverProfile(driverProfileRepo.save(driverProfile()));
        return carRepo.save(car);
    }
}
