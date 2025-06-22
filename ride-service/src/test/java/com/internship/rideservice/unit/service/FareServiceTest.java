package com.internship.rideservice.unit.service;

import com.internship.rideservice.dto.mapper.FareMapper;
import com.internship.rideservice.dto.request.RequestFareDto;
import com.internship.rideservice.dto.response.ResponseFareDto;
import com.internship.rideservice.dto.transfer.FarePackageDto;
import com.internship.rideservice.entity.Fare;
import com.internship.rideservice.enums.FareType;
import com.internship.rideservice.repo.FareRepo;
import com.internship.rideservice.service.command.CommandFareService;
import com.internship.rideservice.service.query.ReadFareService;
import com.internship.rideservice.util.exceptions.ResourceNotFoundException;
import com.internship.rideservice.util.validators.FareValidationManager;
import com.internship.rideservice.utils.FareUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.internship.rideservice.util.exceptions.ExceptionCodes.FARE_NOT_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FareServiceTest {

    @Mock
    private FareRepo fareRepo;

    @Mock
    private FareValidationManager fareValidationManager;

    @Mock
    private FareMapper fareMapper;

    @InjectMocks
    private CommandFareService commandFareService;

    @InjectMocks
    private ReadFareService readFareService;

    @Test
    void createFare_shouldSave_whenValidDto() {
        RequestFareDto dto = FareUtil.validFareDto();
        Fare fare = FareUtil.fareEntity();
        when(fareMapper.handleDto(dto)).thenReturn(fare);

        fareValidationManager.checkForDuplicateType(FareType.ECONOMY);

        commandFareService.createFare(dto);

        verify(fareRepo).save(fare);
    }

    @Test
    void deleteFare_shouldCallRepoDelete_whenFareExists() {
        doNothing().when(fareValidationManager).checkIfNotExistsByType(any());

        commandFareService.deleteFare(FareType.ECONOMY);

        verify(fareRepo).deleteFareByType(any());
    }

    @Test
    void deleteFare_shouldThrow_whenFareDoesNotExist() {
        doThrow(new ResourceNotFoundException(FARE_NOT_FOUND.getCode()))
                .when(fareValidationManager).checkIfNotExistsByType(any());

        assertThatThrownBy(() -> commandFareService.deleteFare(any()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(FARE_NOT_FOUND.getCode());
    }

    @Test
    void getAllFares_shouldReturnAllFares() {
        Fare fare = FareUtil.fareEntity();
        ResponseFareDto dto = FareUtil.responseFareDto();

        when(fareRepo.findAll()).thenReturn(List.of(fare));
        when(fareMapper.handleEntity(fare)).thenReturn(dto);

        FarePackageDto result = readFareService.getAllFares();

        assertThat(result).isNotNull();
        assertThat(result.totalCount()).isEqualTo(1);
    }

    @Test
    void getFareById_shouldReturnFare_whenExists() {
        Fare fare = FareUtil.fareEntity();
        when(fareValidationManager.getFareIfExists(any())).thenReturn(fare);

        Fare result = readFareService.getFareById(FareType.ECONOMY);

        assertThat(result).isNotNull();
        assertThat(result.getType()).isEqualTo(fare.getType());
    }
    @Test
    void getFareById_shouldThrow_whenNotExists() {
        doThrow(new ResourceNotFoundException(FARE_NOT_FOUND.getCode()))
                .when(fareValidationManager).getFareIfExists(any());

        assertThatThrownBy(() -> readFareService.getFareById(any()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(FARE_NOT_FOUND.getCode());
    }
}