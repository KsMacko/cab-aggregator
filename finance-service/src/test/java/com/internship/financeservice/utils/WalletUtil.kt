package com.internship.financeservice.utils

import com.internship.financeservice.dto.response.WalletDto
import com.internship.financeservice.dto.transfer.request.WalletFilterRequest
import com.internship.financeservice.dto.transfer.response.WalletPackageDto
import com.internship.financeservice.entity.DriverWallet
import com.internship.financeservice.utils.UtilConstants.Companion.DEFAULT_ID
import com.internship.financeservice.utils.UtilConstants.Companion.DEFAULT_MONEY
import com.internship.financeservice.utils.UtilConstants.Companion.DEFAULT_PAGE
import com.internship.financeservice.utils.UtilConstants.Companion.DEFAULT_SIZE

object WalletUtil{

    fun validDriverWallet(): DriverWallet {
        return DriverWallet(driverId = DEFAULT_ID).apply {
            balance = DEFAULT_MONEY
        }
    }

    fun walletDto(): WalletDto {
        return WalletDto(
            id = DEFAULT_ID,
            balance = DEFAULT_MONEY,
            driverId = DEFAULT_ID
        )
    }

    fun walletFilterRequest(): WalletFilterRequest {
        return WalletFilterRequest(
            driverId = DEFAULT_ID,
            minBalance = DEFAULT_MONEY,
            maxBalance = DEFAULT_MONEY,
            page = DEFAULT_PAGE,
            size = DEFAULT_SIZE
        )
    }

    fun walletPackageDto(): WalletPackageDto {
        return WalletPackageDto(
            wallets = listOf(walletDto()),
            page = DEFAULT_PAGE,
            size = DEFAULT_SIZE,
            totalPages = 1,
            totalElements = 1
        )
    }
}