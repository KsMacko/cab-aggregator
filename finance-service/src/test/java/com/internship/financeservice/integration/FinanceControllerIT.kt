package com.internship.financeservice.integration

import com.internship.financeservice.config.JsonFiles
import com.internship.financeservice.dto.response.ResponsePaymentDto
import com.internship.financeservice.dto.response.ResponseTransferDto
import com.internship.financeservice.entity.WalletTransfer
import com.internship.financeservice.enums.PaymentType
import com.internship.financeservice.repo.CardRepo
import com.internship.financeservice.repo.DriverWalletRepo
import com.internship.financeservice.repo.FinancialOperationRepo
import com.internship.financeservice.repo.PaymentRepo
import com.internship.financeservice.repo.WalletTransferRepo
import com.internship.financeservice.utils.CardUtil
import com.internship.financeservice.utils.PaymentUtil.validPayment
import com.internship.financeservice.utils.WalletTransferUtil.validWalletTransfer
import com.internship.financeservice.utils.WalletUtil
import com.internship.financeservice.utils.WalletUtil.validDriverWallet
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
import org.springframework.transaction.annotation.Transactional

@Transactional
class FinanceOperationControllerIT(
    @Autowired private val walletTransferRepo: WalletTransferRepo,
    @Autowired private val paymentRepo: PaymentRepo,
    @Autowired private val cardRepo: CardRepo,
    @Autowired private val walletRepo: DriverWalletRepo,
) : BaseTest() {

    @Test
    fun createPaymentByCard_shouldReturnOk_withResponseDto() {
        walletRepo.save(validDriverWallet())
        mockMvc.perform(
            post("${JsonFiles.FINANCE_BASE_URL}/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonFiles.paymentRequest)
        )
            .andExpect(status().isOk)
    }

    @Test
    fun getPaymentById_shouldReturnPaymentDto_whenExists() {
        val payment = paymentRepo.save(validPayment(PaymentType.CARD))
        mockMvc.perform(get("${JsonFiles.FINANCE_BASE_URL}/payments/${payment.id}"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(payment.id))
    }
    @Test
    fun createWalletTransfer_shouldReturnCreated_withLocationHeader() {
        val driverWallet = validWalletTransfer()
        driverWallet.wallet = walletRepo.save(validDriverWallet())
        mockMvc.perform(
            post("${JsonFiles.FINANCE_BASE_URL}/wallet-transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonFiles.walletTransferRequest)
        )
            .andExpect(status().isCreated)
    }

    @Test
    fun getWalletTransferById_shouldReturnTransferDto_whenExists() {
        var driverWalletTransfer = validWalletTransfer()
        driverWalletTransfer.wallet = walletRepo.save(validDriverWallet())
        driverWalletTransfer = walletTransferRepo.save(driverWalletTransfer)
        mockMvc.perform(get("${JsonFiles.FINANCE_BASE_URL}/wallet-transfers/${driverWalletTransfer.id}"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(driverWalletTransfer.id))
    }

}