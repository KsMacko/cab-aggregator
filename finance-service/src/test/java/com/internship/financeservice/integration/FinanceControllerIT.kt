package com.internship.financeservice.integration

import com.internship.financeservice.config.JsonFiles
import com.internship.financeservice.dto.response.ResponsePaymentDto
import com.internship.financeservice.dto.response.ResponseTransferDto
import com.internship.financeservice.repo.CardRepo
import com.internship.financeservice.repo.DriverWalletRepo
import com.internship.financeservice.repo.FinancialOperationRepo
import com.internship.financeservice.repo.PaymentRepo
import com.internship.financeservice.repo.WalletTransferRepo
import com.internship.financeservice.utils.CardUtil
import com.internship.financeservice.utils.WalletUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class FinanceOperationControllerIT(
    @Autowired private val walletRepo: DriverWalletRepo,
    @Autowired private val cardRepo: CardRepo
) : BaseTest() {

    private var createdPayment: ResponsePaymentDto? = null
    private var createdTransfer: ResponseTransferDto? = null

    @BeforeAll
    fun setUp(){
        cardRepo.save(CardUtil.validCard())
        walletRepo.save(WalletUtil.validDriverWallet())
    }

    @Test
    @Order(1)
    fun createPaymentByCard_shouldReturnOk_withResponseDto() {
        val result = mockMvc.perform(
            post("${JsonFiles.FINANCE_BASE_URL}/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonFiles.paymentRequest)
        )
            .andExpect(status().isOk)
            .andReturn()

        val payment = objectMapper.readValue(result.response.contentAsString, ResponsePaymentDto::class.java)
        createdPayment = payment
    }

    @Test
    @Order(2)
    fun getPaymentById_shouldReturnPaymentDto_whenExists() {
        mockMvc.perform(get("${JsonFiles.FINANCE_BASE_URL}/payments/${createdPayment!!.id}"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(createdPayment!!.id))
            .andExpect(jsonPath("$.amount").value(createdPayment!!.amount))
    }
    @Test
    @Order(3)
    fun createWalletTransfer_shouldReturnCreated_withLocationHeader() {
        val result = mockMvc.perform(
            post("${JsonFiles.FINANCE_BASE_URL}/wallet-transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonFiles.walletTransferRequest)
        )
            .andExpect(status().isCreated)
            .andReturn()

        val location = result.response.getHeaderValue("Location").toString()
        val transferId = location.substringAfterLast("/").toLong()

        val transfer = objectMapper.readValue(result.response.contentAsString, ResponseTransferDto::class.java)

        assertThat(transfer.id).isEqualTo(transferId)
        createdTransfer = transfer
    }

    @Test
    @Order(4)
    fun getWalletTransferById_shouldReturnTransferDto_whenExists() {
        mockMvc.perform(get("${JsonFiles.FINANCE_BASE_URL}/wallet-transfers/${createdTransfer!!.id}"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(createdTransfer!!.id))
    }

}