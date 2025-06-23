package com.internship.financeservice.controller

import com.internship.financeservice.dto.mapper.WalletMapper
import com.internship.financeservice.dto.response.WalletDto
import com.internship.financeservice.dto.transfer.request.WalletFilterRequest
import com.internship.financeservice.dto.transfer.response.WalletPackageDto
import com.internship.financeservice.service.wallet.WalletService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/finance/wallet")
class WalletController(
    private val walletService: WalletService,
    private val walletMapper: WalletMapper
) {

    @PostMapping("/driver/{id}")
    fun createWallet(@PathVariable id: Long): ResponseEntity<Void> {
        walletService.createWallet(id)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/driver/{id}")
    fun deleteWallet(@PathVariable id: Long): ResponseEntity<Void> {
        walletService.deleteWallet(id)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/driver/{id}")
    fun getWalletByDriverId(@PathVariable id: Long): ResponseEntity<WalletDto> {
        val wallet = walletService.getWalletById(id)
        return ResponseEntity.ok(walletMapper.toDto(wallet))
    }

    @GetMapping
    fun getAllWallets(@ModelAttribute filter: WalletFilterRequest): ResponseEntity<WalletPackageDto> {
        val walletPackage = walletService.findAllWallets(filter)
        return ResponseEntity.ok(walletPackage)
    }
}