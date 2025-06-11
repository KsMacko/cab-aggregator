package com.internship.rideservice.integration;

import com.internship.rideservice.config.JsonFileReader;
import com.internship.rideservice.entity.PromoCode;
import com.internship.rideservice.repo.PromoCodeRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static com.internship.rideservice.config.JsonFiles.BASE_PROMO_CODES;
import static com.internship.rideservice.config.JsonFiles.BASE_URL;
import static com.internship.rideservice.config.JsonFiles.invalidPromoCodeRequest;
import static com.internship.rideservice.config.JsonFiles.validPromoCodeFilterRequest;
import static com.internship.rideservice.config.JsonFiles.validPromoCodeRequest;
import static com.internship.rideservice.utils.PromoCodeUtil.promoCodeEntity;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PromoCodeControllerIT extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PromoCodeRepo promoCodeRepo;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("Create promo code with valid data - should return 201 and location")
    void createPromoCode_shouldReturnCreated_withLocationHeader() throws Exception {
        mockMvc.perform(post(BASE_PROMO_CODES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validPromoCodeRequest))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, containsString(BASE_PROMO_CODES + "/")))
                .andReturn();
    }

    @Test
    @DisplayName("Try to create promo code with invalid data - should return validation errors")
    void createPromoCode_withInvalidData_shouldReturnValidationError() throws Exception {
        mockMvc.perform(post(BASE_PROMO_CODES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPromoCodeRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    @DisplayName("Get promo code by code after creation - should return 200 OK")
    void getPromoCode_afterCreation_shouldReturnOk() throws Exception {
        PromoCode promoCode = createPromoCode();
        mockMvc.perform(get(BASE_PROMO_CODES + "/{code}/current", promoCode.getPromoCode()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.promoCode").value(promoCode.getPromoCode()));
    }

    @Test
    @DisplayName("Delete promo code by code - should return 204 No Content")
    void deletePromoCode_byCode_shouldReturnNoContent() throws Exception {
        PromoCode promoCode = createPromoCode();
        mockMvc.perform(delete(BASE_PROMO_CODES + "/{id}", promoCode.getId()))
                .andExpect(status().isNoContent());
        assertThat(promoCodeRepo.existsByPromoCode(promoCode.getId())).isFalse();
    }

    @Test
    @DisplayName("Get all promo codes with filter - should return paginated list")
    void getFilteredPromoCodes_withFilter() throws Exception {
        mockMvc.perform(get(BASE_PROMO_CODES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .params(JsonFileReader.parseJsonToQueryParams(validPromoCodeFilterRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.promoCodeDtoList").isArray())
                .andExpect(jsonPath("$.totalElements").value(0));
    }
    private PromoCode createPromoCode(){
        return promoCodeRepo.save(promoCodeEntity());
    }
}