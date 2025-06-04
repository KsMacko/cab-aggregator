package com.internship.financeservice.unit.validation

import com.internship.financeservice.enums.PaymentType
import com.internship.financeservice.repo.PaymentRepo
import com.internship.financeservice.repo.WalletTransferRepo
import com.internship.financeservice.utils.PaymentUtil
import com.internship.financeservice.utils.UtilConstants.Companion.DEFAULT_ID
import com.internship.financeservice.utils.WalletTransferUtil
import com.internship.financeservice.utils.WalletUtil
import com.internship.financeservice.utils.exceptions.ExceptionCodes
import com.internship.financeservice.utils.exceptions.ResourceNotFoundException
import com.internship.financeservice.utils.validation.FinanceValidationManager
import com.internship.financeservice.utils.validation.WalletValidationManager
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class FinanceValidationManagerTest {

    @Mock
    private lateinit var paymentRepo: PaymentRepo

    @Mock
    private lateinit var walletTransferRepo: WalletTransferRepo

    @Mock
    private lateinit var walletValidationManager: WalletValidationManager

    @InjectMocks
    private lateinit var validationManager: FinanceValidationManager

    private val request = WalletTransferUtil.validWalletTransferDto()
    private val wallet = WalletUtil.validDriverWallet()

    @Test
    fun getPaymentOperationIfExists_shouldReturnPayment_whenFound() {
        val expected = PaymentUtil.validPayment(PaymentType.CASH)

        `when`(paymentRepo.findById(DEFAULT_ID)).thenReturn(Optional.of(expected))

        val result = validationManager.getPaymentOperationIfExists(DEFAULT_ID)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun getPaymentOperationIfExists_shouldThrow_whenNotFound() {
        `when`(paymentRepo.findById(DEFAULT_ID)).thenReturn(Optional.empty())

        assertThatThrownBy { validationManager.getPaymentOperationIfExists(DEFAULT_ID) }
            .isInstanceOf(ResourceNotFoundException::class.java)
            .hasMessageContaining(ExceptionCodes.PAYMENT_NOT_FOUND.getCode())
    }

    @Test
    fun getWalletTransferOperationIfExists_shouldReturnTransfer_whenFound() {
        val expected = WalletTransferUtil.validWalletTransfer()

        `when`(walletTransferRepo.findById(DEFAULT_ID)).thenReturn(Optional.of(expected))

        val result = validationManager.getWalletTransferOperationIfExists(DEFAULT_ID)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun getWalletTransferOperationIfExists_shouldThrow_whenNotFound() {
        `when`(walletTransferRepo.findById(DEFAULT_ID)).thenReturn(Optional.empty())

        assertThatThrownBy { validationManager.getWalletTransferOperationIfExists(DEFAULT_ID) }
            .isInstanceOf(ResourceNotFoundException::class.java)
            .hasMessageContaining(ExceptionCodes.WALLET_TRANSFER_NOT_FOUND.getCode())
    }

    @Test
    fun validateWalletTransfer_shouldReturnWallet_whenBalanceIsSufficient() {
        `when`(walletValidationManager.getWalletIfExistsByDriverId(DEFAULT_ID)).thenReturn(wallet)

        val result = validationManager.validateWalletTransfer(request)

        assertThat(result).isEqualTo(wallet)
    }

    @Test
    fun validateWalletTransfer_shouldThrow_whenBalanceIsInsufficient() {
        doThrow(ResourceNotFoundException(ExceptionCodes.WALLET_TRANSFER_INVALID_AMOUNT.getCode()))
            .`when`(walletValidationManager).getWalletIfExistsByDriverId(DEFAULT_ID)

        assertThatThrownBy { validationManager.validateWalletTransfer(request) }
            .isInstanceOf(ResourceNotFoundException::class.java)
            .hasMessageContaining(ExceptionCodes.WALLET_TRANSFER_INVALID_AMOUNT.getCode())
    }

    @Test
    fun validateWalletTransfer_shouldThrow_whenWalletDoesNotExist() {
        `when`(walletValidationManager.getWalletIfExistsByDriverId(DEFAULT_ID))
            .thenThrow(ResourceNotFoundException(ExceptionCodes.WALLET_NOT_FOUND.getCode()))

        assertThatThrownBy { validationManager.validateWalletTransfer(request) }
            .isInstanceOf(ResourceNotFoundException::class.java)
            .hasMessageContaining(ExceptionCodes.WALLET_NOT_FOUND.getCode())
    }
}