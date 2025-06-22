package com.internship.financeservice.unit.service

import com.internship.financeservice.dto.mapper.PaymentMapper
import com.internship.financeservice.dto.mapper.WalletTransferMapper
import com.internship.financeservice.enums.PaymentType
import com.internship.financeservice.repo.DriverWalletRepo
import com.internship.financeservice.repo.FinancialOperationRepo
import com.internship.financeservice.repo.PaymentRepo
import com.internship.financeservice.repo.WalletTransferRepo
import com.internship.financeservice.service.finance.CommandFinanceOperationService
import com.internship.financeservice.service.finance.ReadFinanceOperationService
import com.internship.financeservice.utils.FinancialOperationUtil
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
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

@ExtendWith(MockitoExtension::class)
class FinanceServiceTest{

    @Mock
    private lateinit var financeValidationManager: FinanceValidationManager

    @Mock
    private lateinit var walletTransferMapper: WalletTransferMapper

    @Mock
    private lateinit var paymentRepo: PaymentRepo

    @Mock
    private lateinit var walletTransferRepo: WalletTransferRepo

    @Mock
    private lateinit var financialOperationRepo: FinancialOperationRepo

    @Mock
    private lateinit var walletValidationManager: WalletValidationManager

    @Mock
    private lateinit var walletRepo: DriverWalletRepo

    @Mock
    private lateinit var paymentMapper: PaymentMapper

    @InjectMocks
    private lateinit var commandFinanceOperationService: CommandFinanceOperationService

    @InjectMocks
    private lateinit var readFinanceOperationService: ReadFinanceOperationService

    private val validWalletTransfer = WalletTransferUtil.validWalletTransfer()

    @Test
    fun createPaymentByCash_shouldCreatePaymentOfTypeCash_whenValidEvent() {
        val event = FinancialOperationUtil.confirmedPaymentRequest()
        val financialOp = FinancialOperationUtil.validFinancialOperation()
        val wallet = WalletUtil.validDriverWallet()

        `when`(financialOperationRepo.save(any())).thenReturn(financialOp)
        `when`(paymentRepo.save(any())).thenReturn(PaymentUtil.validPayment(PaymentType.CASH))
        `when`(walletValidationManager.getWalletIfExistsByDriverId(event.driverId)).thenReturn(wallet)

        commandFinanceOperationService.createPaymentByCash(event)

        verify(financialOperationRepo).save(financialOp)
    }

    @Test
    fun createPaymentByCard_shouldReturnMappedPayment_whenValidEvent() {
        val event = FinancialOperationUtil.confirmedPaymentRequest()

        val financialOp = FinancialOperationUtil.financialOperationWithCard()
        val payment = PaymentUtil.validPayment(PaymentType.CARD)
        val driverWallet = WalletUtil.validDriverWallet()

        `when`(financialOperationRepo.save(any())).thenReturn(financialOp)
        `when`(paymentRepo.save(any())).thenReturn(payment)
        `when`(walletValidationManager.getWalletIfExistsByDriverId(event.driverId)).thenReturn(driverWallet)


        val result = commandFinanceOperationService.createPaymentByCard(event)

        assertThat(result.paymentType).isEqualTo(PaymentType.CARD)
        verify(walletRepo).save(any())
    }

    @Test
    fun createWalletTransfer_shouldUpdateBalanceAndSave_whenValidDto() {
        val dto = WalletTransferUtil.validWalletTransferDto()
        val wallet = WalletUtil.validDriverWallet()

        `when`(financeValidationManager.validateWalletTransfer(dto)).thenReturn(wallet)
        `when`(walletTransferMapper.toEntity(dto)).thenReturn(WalletTransferUtil.validWalletTransfer())
        `when`(financialOperationRepo.save(any())).thenAnswer { it.arguments[0] }
        `when`(walletTransferRepo.save(any())).thenAnswer { it.arguments[0] }
        `when`(walletRepo.save(any())).thenAnswer { it.arguments[0] }

        val result = commandFinanceOperationService.createWalletTransfer(dto)

        assertThat(result.remainingAmount).isEqualTo(wallet.balance)
        verify(walletTransferRepo).save(result)
        verify(walletRepo).save(wallet)
    }

    @Test
    fun getPaymentById_shouldThrow_whenNotFound() {
        `when`(financeValidationManager.getPaymentOperationIfExists(DEFAULT_ID))
            .thenThrow(ResourceNotFoundException(ExceptionCodes.PAYMENT_NOT_FOUND.getCode()))

        assertThatThrownBy { readFinanceOperationService.getPaymentById(DEFAULT_ID) }
            .isInstanceOf(ResourceNotFoundException::class.java)
            .hasMessageContaining(ExceptionCodes.PAYMENT_NOT_FOUND.getCode())
    }

    @Test
    fun getWalletTransferById_shouldReturn_whenFound() {
        `when`(financeValidationManager.getWalletTransferOperationIfExists(DEFAULT_ID)).thenReturn(validWalletTransfer)

        val result = readFinanceOperationService.getWalletTransferById(DEFAULT_ID)

        assertThat(result).isEqualTo(validWalletTransfer)
    }

    @Test
    fun getWalletTransferById_shouldThrow_whenNotFound() {
        `when`(financeValidationManager.getWalletTransferOperationIfExists(DEFAULT_ID))
            .thenThrow(ResourceNotFoundException(ExceptionCodes.WALLET_TRANSFER_NOT_FOUND.getCode()))

        assertThatThrownBy { readFinanceOperationService.getWalletTransferById(DEFAULT_ID) }
            .isInstanceOf(ResourceNotFoundException::class.java)
            .hasMessageContaining(ExceptionCodes.WALLET_TRANSFER_NOT_FOUND.getCode())
    }

    @Test
    fun findAllPayments_shouldReturnMappedDto_whenValidFilter() {
        val validPayment = PaymentUtil.validPayment(PaymentType.CASH)
        val filter = PaymentUtil.paymentFilterRequest()
        val page = PageImpl(listOf(validPayment))

        `when`(paymentMapper.toDto(validPayment)).thenReturn(PaymentUtil.responsePaymentDto())
        `when`(paymentRepo.findAll(any(), any<Pageable>())).thenReturn(page)

        val result = readFinanceOperationService.findAllPayments(filter)

        assertThat(result.payments).hasSize(1)
        assertThat(result.totalElements).isEqualTo(1)
        assertThat(result.page).isEqualTo(filter.page)
        assertThat(result.size).isEqualTo(page.size)
        assertThat(result.totalPages).isEqualTo(page.totalPages)
    }

    @Test
    fun findAllWalletTransfers_shouldReturnMappedDto_whenValidFilter() {
        val filter = WalletTransferUtil.walletTransferFilterRequest()
        val page = PageImpl(listOf(validWalletTransfer))

        `when`(walletTransferMapper.toDto(validWalletTransfer)).thenReturn(WalletTransferUtil.responseTransferDto())
        `when`(walletTransferRepo.findAll(any(), any<Pageable>())).thenReturn(page)

        val result = readFinanceOperationService.findAllWalletTransfers(filter)

        assertThat(result.walletTransfers).hasSize(1)
        assertThat(result.totalElements).isEqualTo(1)
        assertThat(result.page).isEqualTo(filter.page)
        assertThat(result.size).isEqualTo(page.size)
        assertThat(result.totalPages).isEqualTo(page.totalPages)
    }
}