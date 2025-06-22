package com.internship.financeservice.unit

import com.internship.financeservice.utils.WalletTransferUtil
import com.internship.financeservice.dto.mapper.WalletTransferMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mapstruct.factory.Mappers
import org.mockito.junit.jupiter.MockitoExtension
import java.time.temporal.ChronoUnit

@ExtendWith(MockitoExtension::class)
class WalletTransferMapperTest {

    private var transferMapper = Mappers.getMapper(WalletTransferMapper::class.java)

    @Test
    fun toDto_shouldMapFieldsFromWalletAndFinancialOperation() {
        val transfer = WalletTransferUtil.validWalletTransfer()
        val expectedDto = WalletTransferUtil.responseTransferDto()

        val result = transferMapper.toDto(transfer)

        assertThat(result.id).isEqualTo(expectedDto.id)
        assertThat(result.driverId).isEqualTo(expectedDto.driverId)
        assertThat(result.date.truncatedTo(ChronoUnit.SECONDS))
            .isEqualTo(expectedDto.date.truncatedTo(ChronoUnit.SECONDS))
        assertThat(result.amount).isEqualTo(expectedDto.amount)
        assertThat(result.remainingAmount).isEqualTo(transfer.remainingAmount)
    }

    @Test
    fun toEntity_shouldIgnoreIgnoredFields_whenValidDto() {
        val dto = WalletTransferUtil.validWalletTransferDto()
        val result = transferMapper.toEntity(dto)
        assertThat(result.id).isNull()
    }
}
