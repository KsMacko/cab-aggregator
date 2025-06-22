package com.internship.financeservice.unit.mappers

import com.internship.financeservice.dto.mapper.CardMapper
import com.internship.financeservice.utils.CardUtil
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.assertj.core.api.Assertions.assertThat
import org.mapstruct.factory.Mappers

@ExtendWith(MockitoExtension::class)
class CardMapperTest {

    private var cardMapper = Mappers.getMapper(CardMapper::class.java)

    @Test
    fun toDto_shouldMapAllFields_whenValidEntity() {
        val entity = CardUtil.validCard()
        val expectedDto = CardUtil.responseCardDto()

        val result = cardMapper.toDto(entity)

        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(expectedDto)
    }

    @Test
    fun toEntity_shouldMapOnlyNonNullFields_whenValidDto() {
        val dto = CardUtil.validCardDto()
        val result = cardMapper.toEntity(dto)
        val expectedEntity = CardUtil.validCard()

        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(expectedEntity)
    }
}