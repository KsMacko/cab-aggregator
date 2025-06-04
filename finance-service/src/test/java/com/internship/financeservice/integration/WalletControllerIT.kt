package com.internship.financeservice.integration

import com.internship.financeservice.config.JsonFiles
import com.internship.financeservice.dto.response.WalletDto
import com.internship.financeservice.repo.DriverWalletRepo
import com.internship.financeservice.utils.UtilConstants.Companion.DEFAULT_ID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class WalletControllerIT: BaseTest() {

    @Test
    @Order(1)
    fun createWallet_shouldReturnOk_whenValidId() {
        mockMvc.perform(
            post("${JsonFiles.WALLET_BASE_URL}/driver/${DEFAULT_ID+1}")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
        )
            .andExpect(status().isOk)
    }

    @Test
    @Order(2)
    fun getWalletByDriverId_shouldReturnWalletDto() {
        val result = mockMvc.perform(get("${JsonFiles.WALLET_BASE_URL}/driver/$DEFAULT_ID"))
            .andExpect(status().isOk)
            .andReturn()

        val wallet = objectMapper.readValue(result.response.contentAsString, WalletDto::class.java)

        assertThat(wallet.driverId).isEqualTo(DEFAULT_ID)
    }

    @Test
    @Order(3)
    fun getAllWallets_shouldReturnNotEmptyList() {
        mockMvc.perform(get(JsonFiles.WALLET_BASE_URL))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.wallets").isArray)
    }

    @Test
    @Order(4)
    fun deleteWallet_shouldReturnNoContent_whenValidId() {
        mockMvc.perform(delete("${JsonFiles.WALLET_BASE_URL}/driver/${DEFAULT_ID+1}"))
            .andExpect(status().isOk)
    }
}