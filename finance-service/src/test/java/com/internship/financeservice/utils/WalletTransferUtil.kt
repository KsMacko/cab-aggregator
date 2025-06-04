package com.internship.financeservice.utils

import com.internship.financeservice.dto.request.RequestWalletTransferDto
import com.internship.financeservice.dto.response.ResponseTransferDto
import com.internship.financeservice.dto.transfer.request.WalletTransferFilterRequest
import com.internship.financeservice.dto.transfer.response.WalletTransferPackageDto
import com.internship.financeservice.entity.WalletTransfer
import com.internship.financeservice.enums.sort.OrderDirection
import com.internship.financeservice.enums.sort.WalletTransferSortFields
import com.internship.financeservice.utils.UtilConstants.Companion.DEFAULT_ID
import com.internship.financeservice.utils.UtilConstants.Companion.DEFAULT_MONEY
import com.internship.financeservice.utils.UtilConstants.Companion.DEFAULT_PAGE
import com.internship.financeservice.utils.UtilConstants.Companion.DEFAULT_SIZE
import java.time.LocalDateTime

object WalletTransferUtil : UtilConstants {

    fun validWalletTransfer(): WalletTransfer {
        val wallet = WalletUtil.validDriverWallet()
        val transfer = WalletTransfer()
        transfer.remainingAmount = wallet.balance
        transfer.wallet = wallet
        transfer.financialOperation = FinancialOperationUtil.validFinancialOperation()
        return transfer
    }

    fun validWalletTransferDto(): RequestWalletTransferDto {
        return RequestWalletTransferDto(
            driverId = DEFAULT_ID,
            amount = DEFAULT_MONEY
        )
    }

    fun responseTransferDto(): ResponseTransferDto {
        return ResponseTransferDto(
            id = DEFAULT_ID,
            driverId = DEFAULT_ID,
            date = LocalDateTime.now(),
            amount = DEFAULT_MONEY,
            remainingAmount = DEFAULT_MONEY
        )
    }

    fun walletTransferFilterRequest(): WalletTransferFilterRequest {
        return WalletTransferFilterRequest(
            createdAt = LocalDateTime.now(),
            driverId = DEFAULT_ID,
            page = DEFAULT_PAGE,
            size = DEFAULT_SIZE,
            orderDirection = OrderDirection.ASC,
            sortBy = WalletTransferSortFields.CREATED_AT
        )
    }

    fun walletTransferPackageDto(): WalletTransferPackageDto {
        return WalletTransferPackageDto(
            walletTransfers = listOf(responseTransferDto()),
            page = DEFAULT_PAGE,
            size = DEFAULT_SIZE,
            totalPages = 1,
            totalElements = 1
        )
    }
}