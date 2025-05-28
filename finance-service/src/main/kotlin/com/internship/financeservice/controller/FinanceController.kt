package com.internship.financeservice.controller

import com.internship.commonevents.event.ConfirmedPaymentRequest
import com.internship.financeservice.dto.mapper.PaymentMapper
import com.internship.financeservice.dto.mapper.WalletTransferMapper
import com.internship.financeservice.dto.request.RequestWalletTransferDto
import com.internship.financeservice.dto.response.ResponsePaymentDto
import com.internship.financeservice.dto.response.ResponseTransferDto
import com.internship.financeservice.dto.transfer.request.PaymentFilterRequest
import com.internship.financeservice.dto.transfer.request.WalletTransferFilterRequest
import com.internship.financeservice.dto.transfer.response.PaymentPackageDto
import com.internship.financeservice.dto.transfer.response.WalletTransferPackageDto
import com.internship.financeservice.service.finance.CommandFinanceOperationService
import com.internship.financeservice.service.finance.ReadFinanceOperationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("api/v1/finance")
class FinanceController (
    private val commandFinanceOperationService: CommandFinanceOperationService,
    private val readFinanceOperationService: ReadFinanceOperationService,
    private val paymentMapper: PaymentMapper,
    private val walletTransferMapper: WalletTransferMapper
){
    @PostMapping("/payment")
    private fun createCardPayment(
        @RequestBody payment: ConfirmedPaymentRequest
    ):ResponseEntity<ResponsePaymentDto>{
        return ResponseEntity.ok(
            paymentMapper.toDto(commandFinanceOperationService.createPaymentByCard(payment))
        )
    }
    @PostMapping("/wallet-transfer")
    private fun createWalletTransfer(
        @RequestBody requestWalletTransferDto: RequestWalletTransferDto
    ):ResponseEntity<ResponseTransferDto>{
        val  walletTransfer = commandFinanceOperationService.createWalletTransfer(requestWalletTransferDto)
        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(walletTransfer.id)
            .toUri()
        return ResponseEntity
            .created(location)
            .body(walletTransferMapper.toDto(walletTransfer))
    }
    @GetMapping("/payments")
    fun getAllPayments(
        @ModelAttribute filter: PaymentFilterRequest
    ): ResponseEntity<PaymentPackageDto> {
        val paymentPackage = readFinanceOperationService.findAllPayments(filter)
        return ResponseEntity.ok(paymentPackage)
    }

    @GetMapping("/payments/{id}")
    fun getPaymentById(@PathVariable id: Long): ResponseEntity<ResponsePaymentDto> {
        val payment = readFinanceOperationService.getPaymentById(id)
        return ResponseEntity.ok(paymentMapper.toDto(payment))
    }

    @GetMapping("/wallet-transfers")
    fun getAllWalletTransfers(
        @ModelAttribute filter: WalletTransferFilterRequest
    ): ResponseEntity<WalletTransferPackageDto> {
        val transferPackage = readFinanceOperationService.findAllWalletTransfers(filter)
        return ResponseEntity.ok(transferPackage)
    }
    @GetMapping("/wallet-transfers/{id}")
    fun getAllWalletTransferById(
        @PathVariable id: Long): ResponseEntity<ResponseTransferDto> {
        val walletTransfer = readFinanceOperationService.getWalletTransferById(id)
        return ResponseEntity.ok(walletTransferMapper.toDto(walletTransfer))
    }
}