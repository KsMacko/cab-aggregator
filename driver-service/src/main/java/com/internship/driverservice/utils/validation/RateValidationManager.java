package com.internship.driverservice.utils.validation;

import com.internship.commonevents.event.RideParticipantsConfirmation;
import com.internship.driverservice.dto.request.RequestRateDto;
import com.internship.driverservice.entity.Rate;
import com.internship.driverservice.repo.RateRepo;
import com.internship.driverservice.utils.exceptions.InvalidInputException;
import com.internship.driverservice.utils.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.internship.driverservice.utils.exceptions.ExceptionCodes.DRIVER_ID_NOT_MATCH_RIDE;
import static com.internship.driverservice.utils.exceptions.ExceptionCodes.PASSENGER_ID_NOT_MATCH_RIDE;
import static com.internship.driverservice.utils.exceptions.ExceptionCodes.RATE_AUTHOR_NOT_MATCH;
import static com.internship.driverservice.utils.exceptions.ExceptionCodes.RATE_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class RateValidationManager {
    private final RateRepo rateRepo;

    public void checkRateAuthor(Long passengerId, Long rideId) {
        Rate rate = rateRepo.findById(rideId)
                .orElseThrow(()-> new ResourceNotFoundException(RATE_NOT_FOUND.getCode()));
        if(!rate.getAuthorId().equals(passengerId)){
            throw new InvalidInputException(RATE_AUTHOR_NOT_MATCH.getCode());
        }
    }
    public void checkParticipants(RideParticipantsConfirmation participants, RequestRateDto requestRate) {
        if(!participants.driverId().equals(requestRate.recipientId())){
            throw new InvalidInputException(DRIVER_ID_NOT_MATCH_RIDE.getCode());
        }
        if(!participants.passengerId().equals(requestRate.authorId())){
            throw new InvalidInputException(PASSENGER_ID_NOT_MATCH_RIDE.getCode());
        }
    }
}
