package com.internship.driverservice.unit.validation;

import com.internship.commonevents.event.RideParticipantsConfirmation;
import com.internship.driverservice.dto.request.RequestRateDto;
import com.internship.driverservice.entity.Rate;
import com.internship.driverservice.repo.RateRepo;
import com.internship.driverservice.utils.exceptions.ExceptionCodes;
import com.internship.driverservice.utils.exceptions.InvalidInputException;
import com.internship.driverservice.utils.validation.RateValidationManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RateValidationManagerTest {

    @Mock
    private RateRepo rateRepo;

    @InjectMocks
    private RateValidationManager validationManager;

    private static final Long RIDE_ID = 1L;
    private static final Long PASSENGER_ID = 100L;

    @Test
    void checkRateAuthor_throwsExceptionIfNotMatched() {
        Rate rate = mock(Rate.class);
        when(rate.getAuthorId()).thenReturn(999L);
        when(rateRepo.findById(RIDE_ID)).thenReturn(Optional.of(rate));

        assertThatThrownBy(() -> validationManager.checkRateAuthor(PASSENGER_ID, RIDE_ID))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(ExceptionCodes.RATE_AUTHOR_NOT_MATCH.getCode());
    }

    @Test
    void checkRateAuthor_doesNotThrowIfMatched() {
        Rate rate = mock(Rate.class);
        when(rate.getAuthorId()).thenReturn(PASSENGER_ID);
        when(rateRepo.findById(RIDE_ID)).thenReturn(Optional.of(rate));

        assertThatCode(() -> validationManager.checkRateAuthor(PASSENGER_ID, RIDE_ID))
                .doesNotThrowAnyException();
    }

    @Test
    void checkParticipants_passengerIdMatches() {
        RideParticipantsConfirmation participants = new RideParticipantsConfirmation(PASSENGER_ID, 2L);
        RequestRateDto requestRate = new RequestRateDto(3, 5L, 5L, "comment");

        assertThatThrownBy(() -> validationManager.checkParticipants(participants, requestRate))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(ExceptionCodes.PASSENGER_ID_NOT_MATCH_RIDE.getCode());
    }

    @Test
    void checkParticipants_driverIdMatches() {
        RideParticipantsConfirmation participants = new RideParticipantsConfirmation(1L, 2L);
        RequestRateDto requestRate = new RequestRateDto(3, 4L, 5L, "comment");

        assertThatThrownBy(() -> validationManager.checkParticipants(participants, requestRate))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(ExceptionCodes.PASSENGER_ID_NOT_MATCH_RIDE.getCode());
    }
}