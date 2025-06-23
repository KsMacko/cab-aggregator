package com.internship.financeservice.entity

import com.internship.financeservice.enums.PaymentType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.MapsId
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "payment")
class Payment (){
    constructor(passengerId: Long, paymentType: PaymentType, financialOperation: FinancialOperation) : this() {
        this.passengerId = passengerId
        this.paymentType = paymentType
        this.financialOperation=financialOperation
    }
    @Id
    val id: Long? = null
    var passengerId: Long = 0
    @Enumerated(EnumType.STRING)
    lateinit var paymentType: PaymentType

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    lateinit var financialOperation: FinancialOperation

    companion object Fields {
        const val ID = "id"
        const val PASSENGER_ID = "passengerId"
        const val PAYMENT_TYPE = "paymentType"
        const val FINANCIAL_OPERATION = "financialOperation"
    }
}