package com.internship.passengerservice.unit.service;

import com.internship.passengerservice.dto.mapper.RateMapper;
import com.internship.passengerservice.dto.request.RequestRateDto;
import com.internship.passengerservice.dto.response.ResponseRateDto;
import com.internship.passengerservice.entity.Rate;
import com.internship.passengerservice.repo.RateRepo;
import com.internship.passengerservice.service.CommandPassengerProfileService;
import com.internship.passengerservice.service.RateService;
import com.internship.passengerservice.service.communication.DriverFeignClient;
import com.internship.passengerservice.util.RateUtil;
import com.internship.passengerservice.utils.ProfileValidationManager;
import com.internship.passengerservice.utils.RateValidationManager;
import com.internship.passengerservice.utils.exceptions.InvalidInputException;
import com.internship.passengerservice.utils.exceptions.ResourceNotFoundException;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.internship.passengerservice.util.UtilConstants.VALID_ID;
import static com.internship.passengerservice.utils.exceptions.ExceptionCodes.PASSENGER_NOT_FOUND;
import static com.internship.passengerservice.utils.exceptions.ExceptionCodes.RATE_AUTHOR_NOT_MATCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RateServiceTest {
    @InjectMocks
    private CommandPassengerProfileService passengerProfileService;

    @InjectMocks
    private RateService rateService;

    @Mock
    private RateRepo rateRepo;

    @Mock
    private ProfileValidationManager profileValidationManager;


    @Mock
    private RateValidationManager rateValidationManager;

    @Mock
    private DriverFeignClient driverFeignClient;

    @Mock
    private RateMapper rateMapper;

    private static final RequestRateDto dto = RateUtil.validRateDto();

    @Test
    void setNewRate_shouldSaveAndReturnDto() {
        Rate rateEntity = RateUtil.validRateEntity();
        ResponseRateDto response = RateUtil.responseRateDto();

        doNothing().when(profileValidationManager).checkIfProfileExists(dto.recipientId());

        when(rateMapper.handleDto(dto)).thenReturn(rateEntity);
        when(rateRepo.save(rateEntity)).thenReturn(rateEntity);
        when(rateMapper.handleEntity(rateEntity)).thenReturn(response);

        ResponseRateDto result = passengerProfileService.setNewRate(dto);

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(response.id());
        assertThat(result.value()).isEqualTo(response.value());
    }

    @Test
    void setNewRate_shouldThrow() {
        doThrow(new ResourceNotFoundException(PASSENGER_NOT_FOUND.getCode()))
                .when(profileValidationManager).checkIfProfileExists(dto.recipientId());

        assertThatThrownBy(() -> passengerProfileService.setNewRate(dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(PASSENGER_NOT_FOUND.getCode());
    }

    @Test
    void deleteRate_shouldDelete() {
        passengerProfileService.deleteRate(VALID_ID, VALID_ID);

        verify(rateRepo, times(1)).deleteById(VALID_ID);
    }

    @Test
    void deleteRate_shouldThrow() {
        doThrow(new InvalidInputException(RATE_AUTHOR_NOT_MATCH.getCode()))
                .when(rateValidationManager).checkRateAuthor(VALID_ID, VALID_ID);

        assertThatThrownBy(() -> passengerProfileService.deleteRate(VALID_ID, VALID_ID))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(RATE_AUTHOR_NOT_MATCH.getCode());
    }
    @Test
    void setRateToDriver_shouldCallFeignClientAndReturnResponse() {
        ResponseRateDto expectedResponse = RateUtil.responseRateDto();

        when(driverFeignClient.setRateToDriver(dto)).thenReturn(expectedResponse);

        ResponseRateDto result = rateService.setRateToDriver(dto);

        assertThat(result).isEqualTo(expectedResponse);
        verify(driverFeignClient, times(1)).setRateToDriver(dto);
    }

    @Test
    void setRateToDriver_shouldThrowExceptionFromFeignClient() {
        FeignException exception = mock(FeignException.class);
        when(driverFeignClient.setRateToDriver(dto)).thenThrow(exception);

        assertThatThrownBy(() -> rateService.setRateToDriver(dto))
                .isInstanceOf(FeignException.class);
    }

    @Test
    void deleteRateFromDriver_shouldCallFeignClient() {
        rateService.deleteRateFromDriver(VALID_ID, VALID_ID);

        verify(driverFeignClient, times(1)).deleteRateFromDriver(VALID_ID, VALID_ID);
    }

    @Test
    void deleteRateFromDriver_shouldThrowExceptionFromFeignClient() {
        FeignException exception = mock(FeignException.class);
        doThrow(exception).when(driverFeignClient).deleteRateFromDriver(VALID_ID, VALID_ID);

        assertThatThrownBy(() -> rateService.deleteRateFromDriver(VALID_ID, VALID_ID))
                .isInstanceOf(FeignException.class);
    }
}
