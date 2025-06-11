package com.internship.driverservice.unit.service.command;

import com.internship.driverservice.dto.mapper.CarMapper;
import com.internship.driverservice.dto.request.RequestCarDto;
import com.internship.driverservice.entity.Car;
import com.internship.driverservice.entity.DriverProfile;
import com.internship.driverservice.repo.CarRepo;
import com.internship.driverservice.service.command.CommandCarService;
import com.internship.driverservice.util.CarUtil;
import com.internship.driverservice.utils.validation.CarValidationManager;
import com.internship.driverservice.utils.validation.ProfileValidationManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("CommandCarService unit tests")
@ExtendWith(MockitoExtension.class)
class CommandCarServiceTest {

    @Mock
    private CarRepo carRepo;

    @Mock
    private CarMapper carMapper;

    @Mock
    private ProfileValidationManager profileValidationManager;

    @Mock
    private CarValidationManager carValidationManager;

    @InjectMocks
    private CommandCarService commandCarService;

    @Test
    @DisplayName("addNewCar creates and saves new car")
    void addNewCar() {
        RequestCarDto dto = CarUtil.requestCarDto();
        DriverProfile driverProfile = CarUtil.createDriverProfile();
        Car mappedCar = CarUtil.carEntity();

        when(profileValidationManager.getDriverProfile(dto.driverId())).thenReturn(driverProfile);
        when(carMapper.handleDto(dto)).thenReturn(mappedCar);
        when(carRepo.save(any(Car.class))).thenReturn(mappedCar);

        Car result = commandCarService.addNewCar(dto);

        verify(carValidationManager).validateCarNumberUniqueness(dto.carNumber());
        verify(carRepo).save(mappedCar);
        assertThat(result).isEqualTo(mappedCar);
    }

    @Test
    @DisplayName("setCurrentCar sets isCurrent true for selected car and false for others")
    void setCurrentCar_setsCorrectly() {
        Long driverProfileId = 1L;
        Long carId = 2L;

        Car currentCar = CarUtil.carEntity();
        Car notCurrentCar = CarUtil.carNotCurrentEntity();

        when(carValidationManager.getIfExistsById(carId)).thenReturn(notCurrentCar);
        when(carRepo.findAllByDriverProfile_ProfileId(driverProfileId))
                .thenReturn(List.of(currentCar, notCurrentCar));
        when(carRepo.save(any(Car.class))).thenReturn(notCurrentCar);

        Car car = commandCarService.setCurrentCar(carId);

        verify(carRepo).findAllByDriverProfile_ProfileId(driverProfileId);
        verify(carRepo).saveAll(anyList());

        assertThat(car.getId()).isEqualTo(notCurrentCar.getId());
        assertThat(car.getIsCurrent()).isTrue();
        assertThat(currentCar.getIsCurrent()).isFalse();
    }
}