package com.internship.financeservice.utils

import com.internship.financeservice.dto.request.RequestCardDto
import com.internship.financeservice.dto.response.ResponseCardDto
import com.internship.financeservice.dto.transfer.request.CardFilterRequest
import com.internship.financeservice.dto.transfer.response.CardPackageDto
import com.internship.financeservice.entity.Card
import com.internship.financeservice.enums.CardType
import com.internship.financeservice.enums.OwnerType
import com.internship.financeservice.enums.sort.CardSortFields
import com.internship.financeservice.enums.sort.OrderDirection
import com.internship.financeservice.utils.UtilConstants.Companion.DEFAULT_ID
import com.internship.financeservice.utils.UtilConstants.Companion.DEFAULT_PAGE
import com.internship.financeservice.utils.UtilConstants.Companion.DEFAULT_SIZE
import com.internship.financeservice.utils.UtilConstants.Companion.VALID_EXPIRATION_DATE
import com.internship.financeservice.utils.UtilConstants.Companion.VALID_LAST_FOUR_DIGITS

object CardUtil : UtilConstants {

    fun validCard(): Card {
        return Card().apply {
            lastFourDigits = VALID_LAST_FOUR_DIGITS
            expirationDate = VALID_EXPIRATION_DATE
            owner = OwnerType.PASSENGER
            ownerId = DEFAULT_ID
            cardType = CardType.VISA
        }
    }

    fun validCardDto(): RequestCardDto {
        return RequestCardDto(
            lastFourDigits = VALID_LAST_FOUR_DIGITS,
            expirationDate = VALID_EXPIRATION_DATE,
            owner = OwnerType.PASSENGER,
            ownerId = DEFAULT_ID,
            cardType = CardType.VISA
        )
    }

    fun responseCardDto(): ResponseCardDto {
        return ResponseCardDto(
            id = DEFAULT_ID,
            lastFourDigits = VALID_LAST_FOUR_DIGITS,
            expirationDate = VALID_EXPIRATION_DATE,
            owner = OwnerType.PASSENGER,
            ownerId = DEFAULT_ID,
            cardType = CardType.VISA
        )
    }

    fun cardFilterRequest(): CardFilterRequest {
        return CardFilterRequest(
            ownerType = OwnerType.PASSENGER,
            cardType = CardType.MASTERCARD,
            ownerId = DEFAULT_ID,
            page = DEFAULT_PAGE,
            size = DEFAULT_SIZE,
            orderDirection = OrderDirection.ASC,
            sortBy = CardSortFields.OWNER_TYPE
        )
    }

    fun cardPackageDto(): CardPackageDto {
        return CardPackageDto(
            cards = listOf(responseCardDto()),
            page = DEFAULT_PAGE,
            size = DEFAULT_SIZE,
            totalPages = 1,
            totalElements = 1
        )
    }
}