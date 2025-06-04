package com.internship.financeservice.integration

import com.internship.financeservice.config.JsonFiles
import com.internship.financeservice.dto.response.ResponseCardDto
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import org.testcontainers.shaded.org.hamcrest.Matchers

class CardControllerIT : BaseTest() {

    private var createdCard: ResponseCardDto? = null

    @Test
    @Order(1)
    fun createCard_shouldReturnCreated_withLocationHeader() {
        val result = mockMvc.perform(
            post(JsonFiles.CARD_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonFiles.cardRequest)
        )
            .andExpect(status().isCreated)
            .andExpect(header().string(HttpHeaders.LOCATION, containsString(JsonFiles.CARD_BASE_URL)))
            .andReturn()

        val location = result.response.getHeaderValue(HttpHeaders.LOCATION).toString()
        val cardId = location.substringAfterLast("/").toLong()

        val response = result.response.contentAsString
        val actual = objectMapper.readValue(response, ResponseCardDto::class.java)

        assertThat(actual.id).isEqualTo(cardId)
        createdCard = actual
    }

    @Test
    @Order(2)
    fun getCardById_shouldReturnCardDto_whenExists() {
        mockMvc.perform(get("${JsonFiles.CARD_BASE_URL}/${createdCard!!.id}"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(createdCard!!.id))
            .andExpect(jsonPath("$.lastFourDigits").value(createdCard!!.lastFourDigits))
            .andExpect(jsonPath("$.cardType").value(createdCard!!.cardType.toString()))
    }

    @Test
    @Order(3)
    fun getAllCards_shouldReturnNotEmptyList() {
        mockMvc.perform(get(JsonFiles.CARD_BASE_URL))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.cards").isArray)
    }

    @Test
    @Order(4)
    fun deleteCard_shouldReturnNoContent_whenValidId() {
        mockMvc.perform(delete("${JsonFiles.CARD_BASE_URL}/${createdCard!!.id}"))
            .andExpect(status().isNoContent)
    }
}