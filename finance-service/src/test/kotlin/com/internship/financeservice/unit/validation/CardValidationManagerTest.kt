package com.internship.financeservice.unit.validation


import com.internship.financeservice.repo.CardRepo
import com.internship.financeservice.utils.CardUtil
import com.internship.financeservice.utils.UtilConstants.Companion.DEFAULT_ID
import com.internship.financeservice.utils.exceptions.ExceptionCodes
import com.internship.financeservice.utils.exceptions.ResourceNotFoundException
import com.internship.financeservice.utils.validation.CardValidationManager
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class CardValidationManagerTest {

    @Mock
    private lateinit var cardRepo: CardRepo

    @InjectMocks
    private lateinit var validationManager: CardValidationManager

    @Test
    fun getCardIfExists_shouldReturnCard_whenFound() {
        val validCard = CardUtil.validCard()
        `when`(cardRepo.findById(DEFAULT_ID)).thenReturn(Optional.of(validCard))

        val result = validationManager.getCardIfExists(DEFAULT_ID)

        assertThat(result).isEqualTo(validCard)
    }

    @Test
    fun getCardIfExists_shouldThrow_whenNotFound() {
        `when`(cardRepo.findById(DEFAULT_ID)).thenReturn(Optional.empty())

        assertThatThrownBy { validationManager.getCardIfExists(DEFAULT_ID) }
            .isInstanceOf(ResourceNotFoundException::class.java)
            .hasMessageContaining(ExceptionCodes.CARD_NOT_FOUND.getCode())
    }

    @Test
    fun checkCardIfExists_shouldNotThrow_whenCardExists() {
        `when`(cardRepo.existsById(DEFAULT_ID)).thenReturn(true)

        validationManager.checkCardIfExists(DEFAULT_ID)

        verify(cardRepo).existsById(DEFAULT_ID)
    }

    @Test
    fun checkCardIfExists_shouldThrow_whenCardDoesNotExist() {
        `when`(cardRepo.existsById(DEFAULT_ID)).thenReturn(false)

        assertThatThrownBy { validationManager.checkCardIfExists(DEFAULT_ID) }
            .isInstanceOf(ResourceNotFoundException::class.java)
            .hasMessageContaining(ExceptionCodes.CARD_NOT_FOUND.getCode())
    }

    @Test
    fun validateCardExistsForOwner_shouldNotThrow_whenOwnerHasCard() {
        `when`(cardRepo.existsCardByOwnerId(DEFAULT_ID)).thenReturn(true)

        validationManager.validateCardExistsForOwner(DEFAULT_ID)

        verify(cardRepo).existsCardByOwnerId(DEFAULT_ID)
    }

    @Test
    fun validateCardExistsForOwner_shouldThrow_whenOwnerHasNoCard() {
        `when`(cardRepo.existsCardByOwnerId(DEFAULT_ID)).thenReturn(false)

        assertThatThrownBy { validationManager.validateCardExistsForOwner(DEFAULT_ID) }
            .isInstanceOf(ResourceNotFoundException::class.java)
            .hasMessageContaining(ExceptionCodes.CARD_NOT_FOUND.getCode())
    }
}