package com.internship.financeservice.controller

import com.internship.financeservice.dto.mapper.CardMapper
import com.internship.financeservice.dto.request.RequestCardDto
import com.internship.financeservice.dto.response.ResponseCardDto
import com.internship.financeservice.dto.transfer.request.CardFilterRequest
import com.internship.financeservice.dto.transfer.response.CardPackageDto
import com.internship.financeservice.service.card.CommandCardService
import com.internship.financeservice.service.card.ReadCardService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/api/v1/finance/cards")
class CardController(
    private val readCardService: ReadCardService,
    private val commandCardService: CommandCardService,
    private val cardMapper: CardMapper
) {
    @GetMapping
    fun getAllCards(@ModelAttribute
                    filter: CardFilterRequest): ResponseEntity<CardPackageDto> {
        val cardPackage = readCardService.findAllCards(filter)
        return ResponseEntity.ok(cardPackage)
    }

    @GetMapping("/{id}")
    fun getCardById(@PathVariable id: Long): ResponseEntity<ResponseCardDto> {
        val card = readCardService.findCardById(id)
        return ResponseEntity.ok(cardMapper.toDto(card))
    }

    @PostMapping
    fun createCard(@RequestBody dto: RequestCardDto): ResponseEntity<ResponseCardDto> {
        val createdCard = commandCardService.createCard(dto)
        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdCard.id)
            .toUri()

        return ResponseEntity
            .created(location)
            .body(cardMapper.toDto(createdCard))
    }

    @DeleteMapping("/{id}")
    fun deleteCard(@PathVariable id: Long): ResponseEntity<Void> {
        commandCardService.deleteCard(id)
        return ResponseEntity.noContent().build()
    }
}