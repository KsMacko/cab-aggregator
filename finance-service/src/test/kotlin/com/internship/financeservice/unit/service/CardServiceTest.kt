package com.internship.financeservice.unit.service

import com.internship.financeservice.dto.mapper.CardMapper
import com.internship.financeservice.repo.CardRepo
import com.internship.financeservice.service.card.CommandCardService
import com.internship.financeservice.service.card.ReadCardService
import com.internship.financeservice.utils.CardUtil
import com.internship.financeservice.utils.UtilConstants.Companion.DEFAULT_ID
import com.internship.financeservice.utils.exceptions.ExceptionCodes
import com.internship.financeservice.utils.exceptions.ResourceNotFoundException
import com.internship.financeservice.utils.validation.CardValidationManager
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class CardServiceTest{

    @Mock
    private lateinit var cardRepo: CardRepo

    @Mock
    private lateinit var cardValidationManager: CardValidationManager

    @Mock
    private lateinit var cardMapper: CardMapper

    @InjectMocks
    private lateinit var commandCardService: CommandCardService

    @InjectMocks
    private lateinit var readCardService: ReadCardService

    private val validCard = CardUtil.validCard()

    @Test
    fun createCard_shouldMapAndSave_whenValidDto() {
        val dto = CardUtil.validCardDto()
        val mappedEntity = CardUtil.validCard()

        `when`(cardMapper.toEntity(dto)).thenReturn(mappedEntity)
        `when`(cardRepo.save(mappedEntity)).thenReturn(mappedEntity)

        val result = commandCardService.createCard(dto)

        verify(cardRepo).save(mappedEntity)
        assertThat(result).isEqualTo(mappedEntity)
    }

    @Test
    fun deleteCard_shouldCallDelete_whenExists() {
        doNothing().`when`(cardValidationManager).checkCardIfExists(DEFAULT_ID)

        commandCardService.deleteCard(DEFAULT_ID)

        verify(cardRepo).deleteById(DEFAULT_ID)
    }

    @Test
    fun findCardById_shouldReturnMappedCard_whenFound() {
        `when`(cardValidationManager.getCardIfExists(DEFAULT_ID)).thenReturn(validCard)

        val result = readCardService.findCardById(DEFAULT_ID)

        assertThat(result).isEqualTo(validCard)
    }

    @Test
    fun findCardById_shouldThrow_whenNotFound() {
        doThrow(ResourceNotFoundException(ExceptionCodes.CARD_NOT_FOUND.getCode()))
            .`when`(cardValidationManager).getCardIfExists(DEFAULT_ID)

        assertThatThrownBy { readCardService.findCardById(DEFAULT_ID) }
            .isInstanceOf(ResourceNotFoundException::class.java)
            .hasMessageContaining(ExceptionCodes.CARD_NOT_FOUND.getCode())
    }

    @Test
    fun deleteCard_shouldThrow_whenNotFound() {
        `when`(cardValidationManager.checkCardIfExists(DEFAULT_ID))
            .thenThrow(ResourceNotFoundException(ExceptionCodes.CARD_NOT_FOUND.getCode()))

        assertThatThrownBy { commandCardService.deleteCard(DEFAULT_ID) }
            .isInstanceOf(ResourceNotFoundException::class.java)
            .hasMessageContaining(ExceptionCodes.CARD_NOT_FOUND.getCode())
    }
}