package com.internship.financeservice.entity


import com.internship.financeservice.enums.CardType
import com.internship.financeservice.enums.OwnerType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "card")
class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
    lateinit var lastFourDigits: String
    lateinit var expirationDate: LocalDate

    @Enumerated(EnumType.STRING)
    lateinit var owner: OwnerType
    var ownerId: Long = 0

    @Enumerated(EnumType.STRING)
    lateinit var cardType: CardType

    @OneToMany(mappedBy = "card")
    var financialOperation: List<FinancialOperation>? = null

    companion object Fields {
        const val ID = "id"
        const val LAST_FOUR_DIGITS = "lastFourDigits"
        const val EXPIRATION_DATE = "expirationDate"
        const val OWNER = "owner"
        const val OWNER_ID = "ownerId"
        const val CARD_TYPE = "cardType"
        const val FINANCIAL_OPERATION = "financialOperation"
    }
}