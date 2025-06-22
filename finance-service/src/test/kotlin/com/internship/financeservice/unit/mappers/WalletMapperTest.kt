package com.internship.financeservice.unit.mappers

import com.internship.financeservice.dto.mapper.WalletMapper
import com.internship.financeservice.utils.WalletUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mapstruct.factory.Mappers
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class WalletMapperTest {

    private var walletMapper = Mappers.getMapper(WalletMapper::class.java)

    @Test
    fun toDto_shouldMapAllFields_whenValidEntity() {
        val entity = WalletUtil.validDriverWallet()
        val expectedDto = WalletUtil.walletDto()

        val result = walletMapper.toDto(entity)

        assertThat(result).usingRecursiveComparison().isEqualTo(expectedDto)
    }
}