package com.internship.financeservice.utils

import java.math.BigDecimal
import java.time.LocalDate

interface UtilConstants {
    companion object {
        const val DEFAULT_INT = 1
        const val DEFAULT_ID = 0L
        const val DEFAULT_PAGE = 0
        const val DEFAULT_SIZE = 10
        const val VALID_LAST_FOUR_DIGITS = "4242"
        val DEFAULT_MONEY = BigDecimal("100.00")
        val VALID_EXPIRATION_DATE = LocalDate.now().plusYears(3)
    }
}
