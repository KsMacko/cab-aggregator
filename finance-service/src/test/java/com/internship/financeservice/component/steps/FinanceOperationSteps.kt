package com.internship.financeservice.component.steps

import com.internship.commonevents.event.ConfirmedPaymentRequest
import com.internship.financeservice.dto.mapper.PaymentMapper
import com.internship.financeservice.dto.mapper.WalletTransferMapper
import com.internship.financeservice.dto.request.RequestWalletTransferDto
import com.internship.financeservice.entity.DriverWallet
import com.internship.financeservice.entity.FinancialOperation
import com.internship.financeservice.entity.Payment
import com.internship.financeservice.entity.WalletTransfer
import com.internship.financeservice.enums.PaymentType
import com.internship.financeservice.repo.DriverWalletRepo
import com.internship.financeservice.repo.FinancialOperationRepo
import com.internship.financeservice.repo.PaymentRepo
import com.internship.financeservice.repo.WalletTransferRepo
import com.internship.financeservice.service.finance.CommandFinanceOperationService
import com.internship.financeservice.utils.validation.FinanceValidationManager
import com.internship.financeservice.utils.validation.WalletValidationManager
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.Assert.assertEquals
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.firstValue
import org.mockito.kotlin.mock
import java.math.BigDecimal

class FinanceOperationSteps {
    @Mock
    private lateinit var paymentRepo: PaymentRepo

    @Mock
    private lateinit var walletTransferRepo: WalletTransferRepo

    @Mock
    private lateinit var financialOperationRepo: FinancialOperationRepo

    @Mock
    private lateinit var driverWalletRepo: DriverWalletRepo

    @Mock
    private lateinit var financeValidationManager: FinanceValidationManager

    @Mock
    private lateinit var walletValidationManager: WalletValidationManager

    @Mock
    private lateinit var paymentMapper: PaymentMapper

    @Mock
    private lateinit var walletTransferMapper: WalletTransferMapper

    @InjectMocks
    private lateinit var commandFinanceOperationService: CommandFinanceOperationService

    private var currentPaymentRequest: ConfirmedPaymentRequest? = null
    private var currentTransferRequest: RequestWalletTransferDto? = null

    private lateinit var currentPaymentType: PaymentType

    private var passengerId: Long = 0
    private var driverId: Long = 0
    private var rideCost: BigDecimal = BigDecimal.ONE

    private val wallet = DriverWallet()
    private var walletBalance = BigDecimal("300")
    private val walletTransfer = WalletTransfer()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        wallet.balance= walletBalance

        `when`(financialOperationRepo.save(any<FinancialOperation>())).thenReturn(mock<FinancialOperation>())
        `when`(paymentRepo.save(any<Payment>())).thenReturn(mock<Payment>())

        `when`(driverWalletRepo.save(any<DriverWallet>())).thenReturn(wallet)
        `when`(walletValidationManager.getWalletIfExistsByDriverId(anyLong())).thenReturn(wallet)
        `when`(financeValidationManager.validateWalletTransfer(any<RequestWalletTransferDto>())).thenReturn(wallet)

        `when`(walletTransferMapper.toEntity(any())).thenReturn(walletTransfer)
    }

    @Given("I am a passenger with ID {long} brought to the place by driver with ID {long}")
    fun i_am_a_passenger_brought_to_the_place(passengerId: Long, driverId: Long) {
        this.passengerId = passengerId
        this.driverId = driverId
    }
    @Given("The ride's cost is {bigdecimal}")
    fun the_rides_cost_is(cost: BigDecimal) {
        this.rideCost = cost
    }

    @Given("I have a confirmed cash payment request")
    fun i_have_confirmed_cash_payment_request() {
        this.currentPaymentType = PaymentType.CASH
        createConfirmedPaymentRequest()
    }

    @Given("I have a confirmed card payment request")
    fun i_have_confirmed_card_payment_request() {
        this.currentPaymentType = PaymentType.CARD
        createConfirmedPaymentRequest()
    }

    @Given("I have a wallet transfer request for driver {long} on amount {bigdecimal}")
    fun i_have_wallet_transfer_request_for_driver(driverId: Long, amount: BigDecimal) {
        this.currentTransferRequest = RequestWalletTransferDto(driverId, amount)
        `when`(walletTransferRepo.save(any<WalletTransfer>())).thenReturn(walletTransfer)
    }

    @When("I process it")
    fun i_process_it() {
        if(currentPaymentRequest != null) {
            if (currentPaymentType == PaymentType.CASH) {
                commandFinanceOperationService.createPaymentByCash(currentPaymentRequest!!)
            } else {
                commandFinanceOperationService.createPaymentByCard(currentPaymentRequest!!)
            }
        }
        if (currentTransferRequest != null) {
            commandFinanceOperationService.createWalletTransfer(currentTransferRequest!!)
        }
    }

    @Then("financial operation is created")
    fun financial_operation_is_created() {
        verify(financialOperationRepo, atLeastOnce()).save(any<FinancialOperation>())
    }

    @Then("payment is saved to DB")
    fun payment_is_saved_to_db() {
        verify(paymentRepo).save(any<Payment>())
    }

    @Then("transfer is saved in database")
    fun transfer_is_saved_in_database() {
        verify(walletTransferRepo).save(any<WalletTransfer>())
    }

    @Then("driver wallet is updated")
    fun driver_wallet_is_updated() {
        verify(driverWalletRepo).save(any<DriverWallet>())
    }

    @Then("wallet balance is updated")
    fun wallet_balance_is_updated() {
        val argumentCaptor = ArgumentCaptor.forClass(DriverWallet::class.java)
        verify(driverWalletRepo).save(argumentCaptor.capture())
        val capturedWallet = argumentCaptor.firstValue

        val expectedBalance = BigDecimal("300").subtract(currentTransferRequest?.amount)
        assertEquals(expectedBalance, capturedWallet.balance)
    }


    private fun createConfirmedPaymentRequest() {
        this.currentPaymentRequest = ConfirmedPaymentRequest(passengerId, rideCost, driverId)
    }
}