package com.internship.financeservice.component.steps

import com.internship.financeservice.dto.mapper.CardMapper
import com.internship.financeservice.dto.request.RequestCardDto
import com.internship.financeservice.entity.Card
import com.internship.financeservice.enums.CardType
import com.internship.financeservice.enums.OwnerType
import com.internship.financeservice.repo.CardRepo
import com.internship.financeservice.service.card.CommandCardService
import com.internship.financeservice.utils.validation.CardValidationManager
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import java.time.LocalDate

class CardSteps {

    @Mock
    private lateinit var cardRepo: CardRepo

    @Mock
    private lateinit var cardValidationManager: CardValidationManager

    @Mock
    private lateinit var cardMapper: CardMapper

    @InjectMocks
    private lateinit var commandCardService: CommandCardService

    private var currentCardId: Long = 0

    private lateinit var lastFourDigits: String
    private lateinit var cardType: CardType
    private lateinit var owner: OwnerType
    private var ownerId: Long = 0
    private lateinit var expirationDate: LocalDate

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Given("I have a valid card with last 4 digits {string} and type {string}")
    fun i_have_valid_card_with_number_and_type(number: String, cardType: String) {
        lastFourDigits = number
        this.cardType = CardType.valueOf(cardType)
    }

    @Given("Card's owner is {string} and his ID is {long}")
    fun card_owner_is(owner: String, id: Long) {
        ownerId = id
        this.owner = OwnerType.valueOf(owner)
    }

    @Given("Expiration date is {string}")
    fun card_expiration_date(date: String) {
        expirationDate = LocalDate.parse(date)
    }

    @Given("I have a card with ID {long}")
    fun i_have_card_with_id(id: Long) {
        currentCardId = id
        doNothing().`when`(cardValidationManager)?.checkCardIfExists(id)
    }

    @When("I send card creation request")
    fun i_send_card_creation_request() {
        val mockCard = mock<Card>()
        whenever(cardMapper.toEntity(any<RequestCardDto>())).thenReturn(mockCard)
        `when`(cardRepo.save(any())).thenReturn(mockCard)
        commandCardService.createCard(RequestCardDto(
            cardType = this.cardType,
            owner = this.owner,
            ownerId = this.ownerId,
            expirationDate = this.expirationDate,
            lastFourDigits = this.lastFourDigits
        ))
    }

    @When("I delete card")
    fun i_delete_card() {
        commandCardService.deleteCard(currentCardId)
    }

    @Then("card is removed from database")
    fun card_is_removed_from_database() {
        verify(cardRepo).deleteById(currentCardId)
    }

    @Then("card is saved to database")
    fun card_is_saved_to_database() {
        verify(cardRepo).save(any())
    }
}