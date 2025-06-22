package com.internship.financeservice.service.finance

import com.internship.commonevents.event.ConfirmedPaymentRequest
import com.internship.financeservice.dto.mapper.PaymentMapper
import com.internship.financeservice.dto.request.RequestWalletTransferDto
import com.internship.financeservice.dto.mapper.WalletTransferMapper
import com.internship.financeservice.dto.response.ResponsePaymentDto
import com.internship.financeservice.dto.response.ResponseTransferDto
import com.internship.financeservice.entity.FinancialOperation
import com.internship.financeservice.entity.Payment
import com.internship.financeservice.entity.WalletTransfer
import com.internship.financeservice.enums.PaymentType
import com.internship.financeservice.repo.DriverWalletRepo
import com.internship.financeservice.repo.FinancialOperationRepo
import com.internship.financeservice.repo.PaymentRepo
import com.internship.financeservice.repo.WalletTransferRepo
import com.internship.financeservice.utils.validation.Constants.Companion.SALARY_PERCENT
import com.internship.financeservice.utils.validation.FinanceValidationManager
import com.internship.financeservice.utils.validation.WalletValidationManager
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class CommandFinanceOperationService (
    private val financeValidationManager: FinanceValidationManager,
    private val walletTransferMapper: WalletTransferMapper,
    private val paymentRepo: PaymentRepo,
    private val walletTransferRepo: WalletTransferRepo,
    private val financialOperationRepo: FinancialOperationRepo,
    private val walletValidationManager: WalletValidationManager,
    private val walletRepo: DriverWalletRepo,
    private val paymentMapper: PaymentMapper
) {
    @Transactional
    fun createPaymentByCash(event: ConfirmedPaymentRequest){
        createPayment(PaymentType.CASH, event)
    }
    @Transactional
    fun createPaymentByCard(event: ConfirmedPaymentRequest): Payment{
        return createPayment(PaymentType.CARD, event)
    }

    @Transactional
    fun createWalletTransfer(requestWalletTransferDto: RequestWalletTransferDto): WalletTransfer {
        val wallet = financeValidationManager.validateWalletTransfer(requestWalletTransferDto)
        val walletTransfer = walletTransferMapper.toEntity(requestWalletTransferDto).apply {
            this.financialOperation = createFinancialOperation(requestWalletTransferDto.amount)
        }
        wallet.balance = wallet.balance.subtract(requestWalletTransferDto.amount)
        walletTransfer.remainingAmount = wallet.balance
        walletTransfer.wallet = walletRepo.save(wallet)
        return walletTransferRepo.save(walletTransfer)
    }
    private fun createFinancialOperation(amount: BigDecimal): FinancialOperation =
        financialOperationRepo.save(FinancialOperation(amount))

    private fun createPayment(paymentType: PaymentType, event: ConfirmedPaymentRequest):Payment {
        val financialOperation = createFinancialOperation(event.amount)
        val payment = Payment(event.passengerId, paymentType, financialOperation)
        calculateDriverDeduction(event.amount, event.driverId)
        financialOperationRepo.save(financialOperation)
        return paymentRepo.save(payment)

    }
    private fun calculateDriverDeduction(amount: BigDecimal, driverId: Long){
        val driverWallet = walletValidationManager.getWalletIfExistsByDriverId(driverId)
        driverWallet.balance += (amount.multiply(SALARY_PERCENT))
        walletRepo.save(driverWallet)
    }

}