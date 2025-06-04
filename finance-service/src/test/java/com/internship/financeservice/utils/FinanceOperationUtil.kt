package com.internship.financeservice.utils

import com.internship.commonevents.event.ConfirmedPaymentRequest
import com.internship.financeservice.entity.FinancialOperation
import com.internship.financeservice.utils.UtilConstants.Companion.DEFAULT_ID
import com.internship.financeservice.utils.UtilConstants.Companion.DEFAULT_MONEY

object FinancialOperationUtil : UtilConstants {

    fun validFinancialOperation(): FinancialOperation {
        return FinancialOperation().apply {
            this.amount = DEFAULT_MONEY
        }
    }

    fun financialOperationWithCard(): FinancialOperation {
        return validFinancialOperation().apply {
            this.card = CardUtil.validCard()
        }
    }
    fun confirmedPaymentRequest(): ConfirmedPaymentRequest {
        return ConfirmedPaymentRequest.builder()
            .passengerId(DEFAULT_ID)
            .amount(DEFAULT_MONEY)
            .driverId(DEFAULT_ID)
            .build()
    }
}