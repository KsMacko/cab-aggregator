package com.internship.driverservice.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.internship.driverservice.dto.request.RequestCarDto;
import com.internship.driverservice.repo.CarRepo;
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

import static com.internship.driverservice.config.JsonFiles.invalidJsonCarRequest;
import static com.internship.driverservice.config.JsonFiles.validJsonCarFilterRequest;
import static com.internship.driverservice.config.JsonFiles.validJsonCarRequest;
import static com.internship.driverservice.config.JsonFiles.validJsonProfileRequest;
import static com.internship.driverservice.util.UtilConstants.BASE_URL;
import static com.internship.driverservice.util.UtilConstants.CAR_BASE_URL;
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

public class CarControllerIT extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DriverProfileRepo driverProfileRepo;

    @Autowired
    private CarRepo carRepo;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static Long createdProfileId;
    private static Long createdCarId;
    private static String modifiedJson;

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
    @DisplayName("Add new car - should return 201 Created")
    void addNewCar_shouldReturnCreated_whenValidRequest() throws Exception {
        JsonNode jsonNode = objectMapper.readTree(validJsonCarRequest);
        ObjectNode objectNode = (ObjectNode) jsonNode;
        objectNode.put("driverId", createdProfileId);
        modifiedJson = objectMapper.writeValueAsString(objectNode);
        RequestCarDto expected = objectMapper.readValue(modifiedJson, RequestCarDto.class);
        MvcResult result = mockMvc.perform(post(CAR_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(modifiedJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.carNumber").value(expected.carNumber()))
                .andExpect(jsonPath("$.brand").value(expected.brand()))
                .andReturn();
        JsonNode responseJson = objectMapper.readTree(result.getResponse().getContentAsString());
        createdCarId = responseJson.get("id").asLong();
    }

    @Test
    @Order(3)
    @DisplayName("Get current car by profile ID - should return 200 OK and car data")
    void getCurrentCar_shouldReturnCar_whenProfileHasCurrentCar() throws Exception {
        mockMvc.perform(get(DRIVER_BASE_URL+"/{id}/cars/current", createdProfileId))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    @DisplayName("Get cars with valid filter - should return list of cars")
    void getCars_withValidFilter_shouldReturnCarList() throws Exception {
        mockMvc.perform(get(CAR_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonCarFilterRequest))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    @DisplayName("Set current car - should return updated car with isCurrent = true")
    void setCurrentCar_shouldSetCurrent_whenValidId() throws Exception {
        mockMvc.perform(patch(CAR_BASE_URL + "/{id}", createdCarId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.driverId").value(createdProfileId))
                .andExpect(jsonPath("$.isCurrent").value(true));
    }

    @Test
    @Order(6)
    @DisplayName("Delete car by ID - should return 204 No Content")
    void deleteCar_shouldReturnNoContent_whenValidId() throws Exception {
        mockMvc.perform(delete(CAR_BASE_URL + "/{id}", createdCarId))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(7)
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
    @Order(8)
    @DisplayName("Add car with duplicate number - should return 400 Bad Request")
    void addNewCar_shouldThrowError_whenCarNumberAlreadyExists() throws Exception {
        mockMvc.perform(post(CAR_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(modifiedJson))
                .andExpect(status().isOk());

        mockMvc.perform(post(CAR_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(modifiedJson))
                .andExpect(status().isBadRequest());
    }
    @AfterAll
    void tearDown() {
        carRepo.deleteAll();
        driverProfileRepo.deleteAll();
    }
}
