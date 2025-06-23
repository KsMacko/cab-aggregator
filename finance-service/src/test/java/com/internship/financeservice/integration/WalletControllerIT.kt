package com.internship.financeservice.integration

import com.internship.financeservice.config.JsonFiles
import com.internship.financeservice.dto.response.WalletDto
import com.internship.financeservice.repo.DriverWalletRepo
import com.internship.financeservice.utils.UtilConstants.Companion.DEFAULT_ID
import com.internship.financeservice.utils.WalletUtil.validDriverWallet
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
import org.springframework.web.context.WebApplicationContext

class WalletControllerIT(
    @Autowired private val walletRepo: DriverWalletRepo
): BaseTest() {

    @Test
    fun createWallet_shouldReturnOk_whenValidId() {
        mockMvc.perform(
            post("${JsonFiles.WALLET_BASE_URL}/driver/${DEFAULT_ID+1}")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
        )
            .andExpect(status().isOk)
    }

    @Test
    fun getWalletByDriverId_shouldReturnWalletDto() {
        val wallet = walletRepo.save(validDriverWallet())
        mockMvc.perform(get("${JsonFiles.WALLET_BASE_URL}/driver/${wallet.driverId}"))
            .andExpect(status().isOk)
            .andReturn()
    }

    @Test
    fun getAllWallets_shouldReturnNotEmptyList() {
        mockMvc.perform(get(JsonFiles.WALLET_BASE_URL))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.wallets").isArray)
    }

    @Test
    fun deleteWallet_shouldReturnNoContent_whenValidId() {
        val wallet = walletRepo.save(validDriverWallet())
        mockMvc.perform(delete("${JsonFiles.WALLET_BASE_URL}/driver/${wallet.driverId}"))
            .andExpect(status().isOk)
    }
}