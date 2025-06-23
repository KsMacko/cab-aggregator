package com.internship.financeservice.unit.mappers

import com.internship.financeservice.dto.mapper.PaymentMapper
import com.internship.financeservice.enums.PaymentType
import com.internship.financeservice.utils.PaymentUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mapstruct.factory.Mappers
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class PaymentMapperTest {

    private var paymentMapper: PaymentMapper = Mappers.getMapper(PaymentMapper::class.java)

    @Test
    fun toDto_shouldMapCreatedAtAndAmountFromFinancialOperation() {
        val payment = PaymentUtil.validPayment(PaymentType.CARD)
        val result = paymentMapper.toDto(payment)
        val expectedResult = PaymentUtil.responsePaymentDto()

        assertThat(result)
            .usingRecursiveComparison()
            .ignoringFields("createdAt", "paymentType")
            .isEqualTo(expectedResult)
    }
}
