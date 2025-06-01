package com.internship.rideservice.service.command;

import com.internship.commonevents.event.CashConfirmationRequest;
import com.internship.commonevents.event.ChangeRideStatusEvent;
import com.internship.commonevents.event.ConfirmedPaymentRequest;
import com.internship.commonevents.event.RideParticipantsConfirmation;
import com.internship.commonevents.event.RideNotificationEvent;
import com.internship.rideservice.dto.request.RequestRideDto;
import com.internship.rideservice.dto.response.ResponseRideDto;
import com.internship.rideservice.dto.mapper.RideMapper;
import com.internship.rideservice.entity.Ride;
import com.internship.rideservice.enums.PaymentType;
import com.internship.rideservice.enums.RideStatus;
import com.internship.rideservice.repo.RideRepo;
import com.internship.rideservice.service.communication.FinanceFeignClient;
import com.internship.rideservice.service.communication.KafkaProducer;
import com.internship.rideservice.util.validators.PromoCodeValidationManager;
import com.internship.rideservice.util.validators.RideValidationManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class CommandRideService {

    private final RideRepo rideRepo;
    private final CalculatePriceService calculatePriceService;
    private final RideValidationManager rideValidationManager;
    private final RideMapper rideMapper;
    private final KafkaProducer kafkaProducer;
    private final FinanceFeignClient financeFeignClient;
    private final PromoCodeValidationManager promoCodeValidationManager;

    @Transactional
    public Ride createRide(RequestRideDto rideDto) {
        rideValidationManager.checkIfPassengerExistsById(rideDto.passengerId());
        promoCodeValidationManager.getCurrentPromoCode(rideDto.promoCode());
        Ride ride = rideRepo.save(rideMapper.handleDto(rideDto));
        kafkaProducer.sendRideCreationNotification(
                RideNotificationEvent.builder()
                        .rideId(ride.getId())
                        .pickupLocation(ride.getStartLocation())
                        .endLocation(ride.getEndLocation())
                        .fare(ride.getFareType().toString())
                        .build()
        );
        return ride;
    }
    @Transactional
    public RideParticipantsConfirmation findDriverAndPassengerByRideId(String rideId) {
        Ride ride = rideValidationManager.getRideByIdIfExists(rideId);
        return new RideParticipantsConfirmation(ride.getDriverId(), ride.getPassengerId());
    }

    @Transactional
    public void deleteRide(String rideId) {
        rideValidationManager.checkIfExistsById(rideId);
        rideRepo.deleteById(rideId);
    }
    @Transactional
    public void changeRideStatus(ChangeRideStatusEvent event) {
        RideStatus status = RideStatus.valueOf(event.status());
        switch (status){
            case WAIT_FOR_PASSENGER -> changeRideStatusToWaitingForPassenger(event.rideId());
            case IN_PROGRESS -> changeRideStatusToInProgress(event.rideId());
            case COMPLETED -> changeRideStatusToCompleted(event.rideId());
            case RECALCULATED -> changeRideStatusRecalculated(event.rideId());
            case ACCEPTED -> changeRideStatusToAccepted(event.rideId(), event.driverId());
        }
    }

    @Transactional
    public void changeRideStatusToAccepted(String rideId, Long driverId) {
        Ride ride = rideValidationManager.getRideByIdIfExists(rideId);
        ride.setDriverId(driverId);
        saveRideAndGetStatus(ride, RideStatus.ACCEPTED);
    }

    @Transactional
    public void changeRideStatusToWaitingForPassenger(String rideId) {
        Ride ride = rideValidationManager.getRideByIdIfExists(rideId);
        ride.setStartWaitingTime(LocalTime.now());
        saveRideAndGetStatus(ride, RideStatus.WAIT_FOR_PASSENGER);
    }

    @Transactional
    public void changeRideStatusToInProgress(String rideId) {
        Ride ride = rideValidationManager.getRideByIdIfExists(rideId);
        ride.setStartTime(LocalTime.now());
        saveRideAndGetStatus(ride, RideStatus.IN_PROGRESS);
    }

    @Transactional
    public void changeRideStatusRecalculated(String rideId) {
        Ride ride = rideValidationManager.getRideByIdIfExists(rideId);
        ride.setEndTime(LocalTime.now());
        BigDecimal recalculatedPrice = calculatePriceService.ReCalculatePrice(ride);
        ride.setPrice(recalculatedPrice.setScale(2, RoundingMode.HALF_UP).toString());
        saveRideAndGetStatus(ride, RideStatus.RECALCULATED);
    }

    @Transactional
    public void changeRideStatusToCompleted(String rideId) {
        Ride ride = rideValidationManager.getRideByIdIfExists(rideId);
        if(ride.getPaymentType()== PaymentType.CASH){
            kafkaProducer.sendPaymentByCashConfirmation(
                    CashConfirmationRequest.builder()
                            .driverId(ride.getDriverId())
                            .passengerId(ride.getPassengerId())
                            .rideId(ride.getId())
                            .amount(new BigDecimal(ride.getPrice()))
                            .build()
            );
        }
        else{
            financeFeignClient.createCardPayment(ConfirmedPaymentRequest.builder()
                    .passengerId(ride.getPassengerId())
                    .driverId(ride.getDriverId())
                    .amount(new BigDecimal(ride.getPrice()))
                    .build());
        }
        saveRideAndGetStatus(ride, RideStatus.COMPLETED);
    }

    private void saveRideAndGetStatus(Ride ride, RideStatus rideStatus) {
        ride.setStatus(rideStatus);
        rideMapper.handleEntity(rideRepo.save(ride));
    }
}
