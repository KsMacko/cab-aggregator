package com.internship.driverservice.utils.validation;

import com.internship.driverservice.entity.DriverProfile;
import com.internship.driverservice.entity.Notification;
import com.internship.driverservice.enums.notification.NotificationStatus;
import com.internship.driverservice.enums.notification.NotificationType;
import com.internship.driverservice.repo.DriverProfileRepo;
import com.internship.driverservice.repo.NotificationRepo;
import com.internship.driverservice.utils.exceptions.InvalidInputException;
import com.internship.driverservice.utils.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.internship.driverservice.utils.exceptions.ExceptionCodes.DRIVER_FOR_RIDE_NOT_FOUND;
import static com.internship.driverservice.utils.exceptions.ExceptionCodes.DRIVER_ID_NOT_NULL;
import static com.internship.driverservice.utils.exceptions.ExceptionCodes.DRIVER_NOT_FOUND;
import static com.internship.driverservice.utils.exceptions.ExceptionCodes.DRIVER_PHONE_UNIQUE;
import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
public class ProfileValidationManager {
    private final DriverProfileRepo driverProfileRepo;
    private final NotificationRepo notificationRepo;

    public void checkIfProfileExists(Long id) {
        if (driverProfileRepo.findById(id).isEmpty())
            throw new ResourceNotFoundException(DRIVER_NOT_FOUND.getCode());
    }

    public void checkIfProfileIdNotNull(Long id) {
        if (isNull(id))
            throw new InvalidInputException(DRIVER_ID_NOT_NULL.getCode());
    }

    public DriverProfile getDriverProfile(Long id) {
        return driverProfileRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(DRIVER_NOT_FOUND.getCode()));
    }

    public void checkIfPhoneUnique(String phone) {
        if (driverProfileRepo.existsByPhone(phone))
            throw new InvalidInputException(DRIVER_PHONE_UNIQUE.getCode());
    }
    public DriverProfile findDriverByAcceptedRide(String rideId) {
        Notification notification = notificationRepo.findByRideIdAndStatusAndType(
                rideId, NotificationStatus.ACCEPTED, NotificationType.RIDE_CREATION)
                .orElseThrow(() -> new ResourceNotFoundException(DRIVER_FOR_RIDE_NOT_FOUND.getCode()));
        return notification.getDriverProfile();
    }

}
