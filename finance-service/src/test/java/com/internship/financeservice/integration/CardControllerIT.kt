package com.internship.financeservice.integration

import com.internship.financeservice.config.JsonFiles
import com.internship.financeservice.dto.response.ResponseCardDto
import com.internship.financeservice.entity.Card
import com.internship.financeservice.repo.CardRepo
import com.internship.financeservice.utils.CardUtil.validCard
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

import org.testcontainers.shaded.org.hamcrest.Matchers

@Transactional
class CardControllerIT(
    @Autowired private var cardRepo: CardRepo
) : BaseTest() {

    @Test
    fun createCard_shouldReturnCreated_withLocationHeader() {
        mockMvc.perform(
            post(JsonFiles.CARD_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonFiles.cardRequest)
        )
            .andExpect(status().isCreated)
            .andExpect(header().string(HttpHeaders.LOCATION, containsString(JsonFiles.CARD_BASE_URL)))
    }

    @Test
    fun getCardById_shouldReturnCardDto_whenExists() {
        val card = createCard()
        mockMvc.perform(get("${JsonFiles.CARD_BASE_URL}/${card.id}"))
            .andExpect(status().isOk)
    }

    @Test
    fun getAllCards_shouldReturnNotEmptyList() {
        mockMvc.perform(get(JsonFiles.CARD_BASE_URL))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.cards").isArray)
    }

    @Test
    fun deleteCard_shouldReturnNoContent_whenValidId() {
        mockMvc.perform(delete("${JsonFiles.CARD_BASE_URL}/${createCard().id}"))
            .andExpect(status().isNoContent)
    }
    fun createCard(): Card{
        return cardRepo.save(validCard())
    }
}