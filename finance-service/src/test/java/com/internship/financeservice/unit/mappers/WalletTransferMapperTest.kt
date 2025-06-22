package com.internship.financeservice.unit.mappers

import com.internship.financeservice.utils.WalletTransferUtil
import com.internship.financeservice.dto.mapper.WalletTransferMapper
import com.internship.financeservice.utils.WalletTransferUtil.responseTransferDto
import com.internship.financeservice.utils.WalletTransferUtil.validWalletTransfer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mapstruct.factory.Mappers
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class WalletTransferMapperTest {

    private var transferMapper = Mappers.getMapper(WalletTransferMapper::class.java)

    @Test
    fun toDto_shouldMapFieldsFromWalletAndFinancialOperation() {
        val transfer = validWalletTransfer()
        val expectedDto = responseTransferDto()
        val result = transferMapper.toDto(transfer)

        assertThat(result)
            .usingRecursiveComparison()
            .ignoringFields("date")
            .isEqualTo(expectedDto)
    }

    @Test
    fun toEntity_shouldIgnoreIgnoredFields_whenValidDto() {
        val dto = WalletTransferUtil.validWalletTransferDto()
        val result = transferMapper.toEntity(dto)
        val expectedEntity = validWalletTransfer()

        assertThat(result)
            .usingRecursiveComparison()
            .ignoringFields("financialOperation", "remainingAmount", "wallet")
            .isEqualTo(expectedEntity)

    }
}
