package com.internship.financeservice.utils

import com.internship.financeservice.dto.response.ResponsePaymentDto
import com.internship.financeservice.dto.transfer.request.PaymentFilterRequest
import com.internship.financeservice.dto.transfer.response.PaymentPackageDto
import com.internship.financeservice.entity.Payment
import com.internship.financeservice.enums.PaymentType
import com.internship.financeservice.enums.sort.OrderDirection
import com.internship.financeservice.enums.sort.PaymentSortFields
import com.internship.financeservice.utils.UtilConstants.Companion.DEFAULT_ID
import com.internship.financeservice.utils.UtilConstants.Companion.DEFAULT_MONEY
import com.internship.financeservice.utils.UtilConstants.Companion.DEFAULT_PAGE
import com.internship.financeservice.utils.UtilConstants.Companion.DEFAULT_SIZE
import java.time.LocalDateTime

object PaymentUtil : UtilConstants {

    fun validPayment(type: PaymentType): Payment {
        return Payment(
            passengerId = DEFAULT_ID,
            paymentType = type,
            financialOperation = FinancialOperationUtil.validFinancialOperation())
    }

    fun responsePaymentDto(): ResponsePaymentDto {
        return ResponsePaymentDto(
            id = DEFAULT_ID,
            passengerId = DEFAULT_ID,
            createdAt = LocalDateTime.now(),
            amount = DEFAULT_MONEY,
            paymentType = PaymentType.CASH
        )
    }

    fun paymentFilterRequest(): PaymentFilterRequest {
        return PaymentFilterRequest(
            createdAt = LocalDateTime.now(),
            passengerId = DEFAULT_ID,
            paymentType = PaymentType.CASH,
            page = DEFAULT_PAGE,
            size = DEFAULT_SIZE,
            orderDirection = OrderDirection.DESC,
            sortBy = PaymentSortFields.CREATED_AT
        )
    }

    fun paymentPackageDto(): PaymentPackageDto {
        return PaymentPackageDto(
            payments = listOf(responsePaymentDto()),
            page = DEFAULT_PAGE,
            size = DEFAULT_SIZE,
            totalPages = 1,
            totalElements = 1
        )
    }
}