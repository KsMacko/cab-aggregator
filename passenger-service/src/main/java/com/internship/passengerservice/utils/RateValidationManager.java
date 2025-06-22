package com.internship.passengerservice.utils;

import com.internship.commonevents.event.RideParticipantsConfirmation;
import com.internship.passengerservice.dto.request.RequestRateDto;
import com.internship.passengerservice.entity.Rate;
import com.internship.passengerservice.repo.RateRepo;
import com.internship.passengerservice.utils.exceptions.InvalidInputException;
import com.internship.passengerservice.utils.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.internship.passengerservice.utils.exceptions.ExceptionCodes.DRIVER_ID_NOT_MATCH_RIDE;
import static com.internship.passengerservice.utils.exceptions.ExceptionCodes.PASSENGER_ID_NOT_MATCH_RIDE;
import static com.internship.passengerservice.utils.exceptions.ExceptionCodes.RATE_AUTHOR_NOT_MATCH;
import static com.internship.passengerservice.utils.exceptions.ExceptionCodes.RATE_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class RateValidationManager {
    private final RateRepo rateRepo;

    public void checkRateAuthor(Long driverId, Long rateId) {
        Rate rate = rateRepo.findById(rateId)
                .orElseThrow(()-> new ResourceNotFoundException(RATE_NOT_FOUND.getCode()));
        if(!rate.getAuthorId().equals(driverId)){
            throw new InvalidInputException(RATE_AUTHOR_NOT_MATCH.getCode());
        }
    }
    public void checkParticipants(RideParticipantsConfirmation participants, RequestRateDto requestRate) {
        if(!participants.passengerId().equals(requestRate.recipientId())){
            throw new InvalidInputException(PASSENGER_ID_NOT_MATCH_RIDE.getCode());
        }
        if(!participants.driverId().equals(requestRate.authorId())){
            throw new InvalidInputException(DRIVER_ID_NOT_MATCH_RIDE.getCode());
        }
    }
}
