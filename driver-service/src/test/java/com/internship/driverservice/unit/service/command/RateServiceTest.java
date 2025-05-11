package com.internship.driverservice.unit.service.command;

import com.internship.commonevents.event.RideParticipantsConfirmation;
import com.internship.driverservice.dto.mapper.RateMapper;
import com.internship.driverservice.dto.request.RequestRateDto;
import com.internship.driverservice.dto.response.ResponseRateDto;
import com.internship.driverservice.entity.DriverProfile;
import com.internship.driverservice.entity.Rate;
import com.internship.driverservice.repo.RateRepo;
import com.internship.driverservice.service.command.RateService;
import com.internship.driverservice.service.communication.PassengerFeignClient;
import com.internship.driverservice.service.communication.RideFeignClient;
import com.internship.driverservice.utils.validation.ProfileValidationManager;
import com.internship.driverservice.utils.validation.RateValidationManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.internship.driverservice.util.RateUtil.DEFAULT_AUTHOR_ID;
import static com.internship.driverservice.util.RateUtil.DEFAULT_RATE_ID;
import static com.internship.driverservice.util.RateUtil.DEFAULT_RECIPIENT_ID;
import static com.internship.driverservice.util.RateUtil.rateEntity;
import static com.internship.driverservice.util.RateUtil.requestRateDto;
import static com.internship.driverservice.util.RateUtil.responseRateDto;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RateService unit tests")
class RateServiceTest {

    @Mock
    private RateRepo rateRepo;

    @Mock
    private RateValidationManager rateValidationManager;

    @Mock
    private ProfileValidationManager profileValidationManager;

    @Mock
    private RideFeignClient rideFeignClient;

    @Mock
    private PassengerFeignClient passengerFeignClient;

    @Mock
    private RateMapper rateMapper;

    @InjectMocks
    private RateService rateService;

    @Test
    @DisplayName("setNewRate creates and saves rate")
    void setNewRate_createsAndSaves() {
        RequestRateDto dto = requestRateDto();
        DriverProfile driverProfile = mock(DriverProfile.class);
        RideParticipantsConfirmation confirmation = mock(RideParticipantsConfirmation.class);
        Rate rate = rateEntity();

        when(profileValidationManager.getDriverProfile(dto.recipientId())).thenReturn(driverProfile);
        when(rideFeignClient.checkParticipants(dto.rideId())).thenReturn(confirmation);
        when(rateMapper.handleDto(dto)).thenReturn(rate);
        when(rateRepo.save(rate)).thenReturn(rate);

        Rate result = rateService.setNewRate(dto);

        verify(rateValidationManager).checkParticipants(confirmation, dto);
        verify(rateRepo).save(rate);
        assertThat(result).isEqualTo(rate);
    }

    @Test
    @DisplayName("deleteRate deletes rate after author check")
    void deleteRate_deletesSuccessfully() {;
        rateService.deleteRate(DEFAULT_AUTHOR_ID, DEFAULT_RATE_ID);

        verify(rateValidationManager).checkRateAuthor(DEFAULT_AUTHOR_ID, DEFAULT_RATE_ID);
        verify(rateRepo).deleteById(DEFAULT_RATE_ID);
    }

    @Test
    @DisplayName("setRateToPassenger calls passengerFeignClient with correct DTO")
    void setRateToPassenger_callsFeignClient() {
        RequestRateDto dto = requestRateDto();
        ResponseRateDto response = responseRateDto();

        when(passengerFeignClient.setRateToPassenger(dto)).thenReturn(response);

        ResponseRateDto result = rateService.setRateToPassenger(dto);

        verify(passengerFeignClient).setRateToPassenger(dto);
        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("deleteRateFromPassenger calls passengerFeignClient to delete")
    void deleteRateFromPassenger_callsFeignClient() {
        rateService.deleteRateFromPassenger(DEFAULT_RECIPIENT_ID, DEFAULT_RATE_ID);

        verify(passengerFeignClient).deleteRateFromPassenger(DEFAULT_RECIPIENT_ID, DEFAULT_RATE_ID);
    }
}