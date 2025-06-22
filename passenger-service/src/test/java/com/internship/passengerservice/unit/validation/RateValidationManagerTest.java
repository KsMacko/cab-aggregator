package com.internship.passengerservice.unit.validation;

import com.internship.commonevents.event.RideParticipantsConfirmation;
import com.internship.passengerservice.dto.request.RequestRateDto;
import com.internship.passengerservice.entity.Rate;
import com.internship.passengerservice.repo.RateRepo;
import com.internship.passengerservice.util.ProfileUtil;
import com.internship.passengerservice.util.RateUtil;
import com.internship.passengerservice.utils.RateValidationManager;
import com.internship.passengerservice.utils.exceptions.InvalidInputException;
import com.internship.passengerservice.utils.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.internship.passengerservice.util.UtilConstants.VALID_ID;
import static com.internship.passengerservice.utils.exceptions.ExceptionCodes.PASSENGER_ID_NOT_MATCH_RIDE;
import static com.internship.passengerservice.utils.exceptions.ExceptionCodes.RATE_AUTHOR_NOT_MATCH;
import static com.internship.passengerservice.utils.exceptions.ExceptionCodes.RATE_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RateValidationManagerTest {

    @Mock
    private RateRepo rateRepo;

    @InjectMocks
    private RateValidationManager rateValidationManager;

    @Test
    void checkRateAuthor_shouldThrowInvalidInputException_whenAuthorDoesNotMatch() {
        Rate rate = RateUtil.validRateEntity();
        rate.setAuthorId(VALID_ID+1);
        when(rateRepo.findById(VALID_ID)).thenReturn(Optional.of(rate));

        assertThatThrownBy(() -> rateValidationManager.checkRateAuthor(VALID_ID, VALID_ID))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(RATE_AUTHOR_NOT_MATCH.getCode());
    }

    @Test
    void checkRateAuthor_shouldThrowResourceNotFoundException_whenRateNotFound() {
        when(rateRepo.findById(VALID_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> rateValidationManager.checkRateAuthor(VALID_ID, VALID_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(RATE_NOT_FOUND.getCode());
    }

    @Test
    void checkParticipants_shouldThrowInvalidInputException_whenPassengerIdsDoNotMatch() {
        RideParticipantsConfirmation participants = ProfileUtil.rideParticipantsConfirmation();
        RequestRateDto rateDto = RateUtil.validRateDto();

        assertThatThrownBy(() -> rateValidationManager.checkParticipants(participants, rateDto))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(PASSENGER_ID_NOT_MATCH_RIDE.getCode());
    }

    @Test
    void checkParticipants_shouldNotThrow_whenAllIdsMatch() {
        RideParticipantsConfirmation participants = new RideParticipantsConfirmation(VALID_ID, VALID_ID);
        RequestRateDto rateDto = RateUtil.validRateDto();

        rateValidationManager.checkParticipants(participants, rateDto);

    }
}