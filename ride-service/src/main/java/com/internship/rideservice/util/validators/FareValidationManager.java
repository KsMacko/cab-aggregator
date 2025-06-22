package com.internship.rideservice.util.validators;

import com.internship.rideservice.entity.Fare;
import com.internship.rideservice.enums.FareType;
import com.internship.rideservice.repo.FareRepo;
import com.internship.rideservice.util.exceptions.InvalidInputException;
import com.internship.rideservice.util.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.internship.rideservice.util.exceptions.ExceptionCodes.FARE_ALREADY_EXISTS;
import static com.internship.rideservice.util.exceptions.ExceptionCodes.FARE_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class FareValidationManager {
    private final FareRepo fareRepo;

    public Fare getFareIfExists(FareType fareType) {
        return fareRepo.findFareByType(fareType).stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(FARE_NOT_FOUND.getCode()));
    }

    public void checkForDuplicateType(FareType fareType) {
        if (fareRepo.existsFareByType(fareType)) {
            throw new InvalidInputException(FARE_ALREADY_EXISTS.getCode());
        }
    }

    public void checkIfNotExistsByType(FareType fareType) {
        if (!fareRepo.existsFareByType(fareType)) {
            throw new ResourceNotFoundException(FARE_NOT_FOUND.getCode());
        }
    }
}
