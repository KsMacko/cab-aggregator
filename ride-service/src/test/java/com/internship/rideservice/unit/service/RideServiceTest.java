package com.internship.rideservice.unit.service;

import com.internship.commonevents.event.RideNotificationEvent;
import com.internship.commonevents.event.RideParticipantsConfirmation;
import com.internship.rideservice.dto.mapper.RideMapper;
import com.internship.rideservice.dto.request.RequestRideDto;
import com.internship.rideservice.dto.response.ResponseRideDto;
import com.internship.rideservice.dto.transfer.RideFilterRequest;
import com.internship.rideservice.dto.transfer.RidePackageDto;
import com.internship.rideservice.entity.Ride;
import com.internship.rideservice.enums.RideStatus;
import com.internship.rideservice.repo.RideRepo;
import com.internship.rideservice.service.command.CalculatePriceService;
import com.internship.rideservice.service.command.CommandRideService;
import com.internship.rideservice.service.communication.FinanceFeignClient;
import com.internship.rideservice.service.communication.KafkaProducer;
import com.internship.rideservice.service.query.ReadRideService;
import com.internship.rideservice.util.validators.PromoCodeValidationManager;
import com.internship.rideservice.util.validators.RideValidationManager;
import com.internship.rideservice.utils.PromoCodeUtil;
import com.internship.rideservice.utils.RideUtil;
import com.internship.rideservice.utils.UtilConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RideServiceTest implements UtilConstants {

    @Mock
    private RideRepo rideRepo;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private RideValidationManager rideValidationManager;

    @Mock
    private PromoCodeValidationManager promoCodeValidationManager;

    @Mock
    private KafkaProducer kafkaProducer;

    @Mock
    private CalculatePriceService calculatePriceService;

    @Mock
    private FinanceFeignClient financeFeignClient;

    @Mock
    private RideMapper rideMapper;

    @InjectMocks
    private CommandRideService commandRideService;

    @InjectMocks
    private ReadRideService readRideService;

    private static final Ride validRide = RideUtil.rideEntity();
    private static final RequestRideDto validRideDto = RideUtil.validRideDto();
    private static final ResponseRideDto responseRideDto = RideUtil.responseRideDto();

    @Test
    void createRide_shouldSave_whenValidDto() {
        doNothing().when(rideValidationManager).checkIfPassengerExistsById(anyLong());
        when(promoCodeValidationManager.getCurrentPromoCode(anyString())).thenReturn(PromoCodeUtil.promoCodeEntity());

        when(rideMapper.handleDto(validRideDto)).thenReturn(validRide);
        when(rideRepo.save(validRide)).thenReturn(validRide);

        Ride result = commandRideService.createRide(validRideDto);

        verify(rideRepo).save(validRide);
        verify(kafkaProducer).sendRideCreationNotification(any(RideNotificationEvent.class));
        assertThat(result).isEqualTo(validRide);
    }

    @Test
    void findDriverAndPassengerByRideId_shouldReturnConfirmation_whenValidRideId() {

        when(rideValidationManager.getRideByIdIfExists(anyString())).thenReturn(validRide);

        RideParticipantsConfirmation result = commandRideService.findDriverAndPassengerByRideId(VALID_ID);

        assertThat(result.driverId()).isEqualTo(validRide.getDriverId());
        assertThat(result.passengerId()).isEqualTo(validRide.getPassengerId());
    }

    @Test
    void deleteRide_shouldCallDelete_whenRideExists() {
        doNothing().when(rideValidationManager).checkIfExistsById(anyString());
        commandRideService.deleteRide(VALID_ID);

        verify(rideRepo).deleteById(VALID_ID);
    }

    @Test
    void changeRideStatusToAccepted_shouldSetDriverId_andUpdateStatus() {
        Ride validRide = mock(Ride.class);
        when(rideValidationManager.getRideByIdIfExists(anyString())).thenReturn(validRide);

        commandRideService.changeRideStatusToAccepted(VALID_ID, VALID_PERSON_ID);

        assertThat(validRide.getDriverId()).isEqualTo(VALID_PERSON_ID);
        verify(validRide).setStatus(any(RideStatus.class));
        verify(rideMapper).handleEntity(rideRepo.save(validRide));
    }

    @Test
    void changeRideStatusRecalculated_shouldRecalculatePrice_andUpdateStatus() {
        Ride validRide = mock(Ride.class);

        when(rideValidationManager.getRideByIdIfExists(anyString())).thenReturn(validRide);
        when(calculatePriceService.ReCalculatePrice(any(Ride.class))).thenReturn(VALID_PRICE);

        commandRideService.changeRideStatusRecalculated(VALID_ID);

        verify(validRide).setPrice(any(BigDecimal.class).toString());
        verify(validRide).setStatus(any(RideStatus.class));
    }

    @Test
    void changeRideStatusToCompleted_withCashPayment_shouldSendKafkaEvent() {
        when(rideValidationManager.getRideByIdIfExists(anyString())).thenReturn(validRide);

        commandRideService.changeRideStatusToCompleted(VALID_ID);

        verify(kafkaProducer).sendPaymentByCashConfirmation(
                argThat(request -> request.rideId().equals(VALID_ID) &&
                        request.amount().equals(validRide.getPrice())
                )
        );
        verify(rideMapper).handleEntity(rideRepo.save(validRide));
    }

    @Test
    void getAllRides_shouldReturnMappedPage_withCorrectData() {
        RideFilterRequest filter = RideUtil.validRideFilterRequest();

        when(mongoTemplate.find(any(Query.class), eq(Ride.class))).thenReturn(List.of(validRide));
        when(mongoTemplate.count(any(Query.class), eq(Ride.class))).thenReturn(1L);

        RidePackageDto result = readRideService.getAllRides(filter);

        assertThat(result).isNotNull();
        assertThat(result.ridesDto()).hasSize(List.of(validRide).size());
        assertThat(result.totalElements()).isEqualTo(List.of(validRide).size());
        assertThat(result.pageNumber()).isEqualTo(filter.getPage());
        assertThat(result.pageSize()).isEqualTo(List.of(validRide).size());
        assertThat(result.totalPages()).isEqualTo(1);
    }

    @Test
    void getRideById_shouldReturnMappedResponse_whenExists() {

        when(rideValidationManager.getRideByIdIfExists(anyString())).thenReturn(validRide);

        Ride result = readRideService.getRideById(VALID_ID);

        assertThat(result).usingRecursiveComparison().isEqualTo(validRide);
    }
}