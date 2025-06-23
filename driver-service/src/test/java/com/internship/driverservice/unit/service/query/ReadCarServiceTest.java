package com.internship.driverservice.unit.service.query;

import com.internship.driverservice.dto.mapper.CarMapper;
import com.internship.driverservice.dto.transfer.CarFilterRequest;
import com.internship.driverservice.dto.transfer.CarPackageDto;
import com.internship.driverservice.entity.Car;
import com.internship.driverservice.entity.DriverProfile;
import com.internship.driverservice.repo.CarRepo;
import com.internship.driverservice.service.query.ReadCarService;
import com.internship.driverservice.service.specification.CarSpecification;
import com.internship.driverservice.utils.validation.CarValidationManager;
import com.internship.driverservice.utils.validation.ProfileValidationManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static com.internship.driverservice.util.CarUtil.carNotCurrentEntity;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_ID;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_PAGE;
import static com.internship.driverservice.util.UtilConstants.DEFAULT_PAGE_SIZE;
import static org.mockito.Mockito.eq;
import static com.internship.driverservice.util.CarUtil.carEntity;
import static com.internship.driverservice.util.CarUtil.createDriverProfile;
import static com.internship.driverservice.util.CarUtil.responseCarDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReadCarService unit tests")
class ReadCarServiceTest {

    @Mock
    private CarRepo carRepo;

    @Mock
    private CarMapper carMapper;

    @Mock
    private CarSpecification carSpecification;

    @Mock
    private ProfileValidationManager profileValidationManager;

    @Mock
    private CarValidationManager carValidationManager;

    @Mock
    private CarFilterRequest filter;

    @InjectMocks
    private ReadCarService readCarService;

    @Test
    @DisplayName("readAllCars returns paginated response with cars")
    void readAllCars_returnsCarPackageDto() {
        filter = new CarFilterRequest();
        filter.setPage(DEFAULT_PAGE);
        filter.setPageSize(DEFAULT_PAGE_SIZE);
        Specification<Car> spec = mock(Specification.class);
        Pageable pageable = PageRequest.of(DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
        Page<Car> page = mockCarPage(pageable);

        mockCommonDependencies(spec, page);

        CarPackageDto result = readCarService.readAllCars(filter);

        verify(carRepo).findAll(eq(spec), eq(pageable));
        assertCarPackageDto(result, page);
    }

    @Test
    @DisplayName("readAllCars uses default page and pageSize if not set")
    void readAllCars_usesDefaultValuesWhenPageParamsNull() {
        CarFilterRequest emptyFilter = new CarFilterRequest();
        Specification<Car> spec = mock(Specification.class);
        Pageable expectedPageable = PageRequest.of(DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
        Page<Car> resultPage = mockCarPage(expectedPageable);

        mockCommonDependencies(spec, resultPage);

        CarPackageDto dto = readCarService.readAllCars(emptyFilter);

        verify(carRepo).findAll(eq(spec), eq(expectedPageable));
        verify(carSpecification).createFilterSpecification(argThat((CarFilterRequest f) ->
                f.getPage().equals(DEFAULT_PAGE) && f.getPageSize().equals(DEFAULT_PAGE_SIZE)));

        assertCarPackageDto(dto, resultPage);
    }

    private Page<Car> mockCarPage(Pageable pageable) {
        List<Car> cars = List.of(carEntity(), carNotCurrentEntity());
        return new PageImpl<>(cars, pageable, cars.size());
    }

    private void mockCommonDependencies(Specification<Car> spec, Page<Car> page) {
        when(carSpecification.createFilterSpecification(any(CarFilterRequest.class))).thenReturn(spec);
        when(carRepo.findAll(eq(spec), any(Pageable.class))).thenReturn(page);
        when(carMapper.handleEntity(any(Car.class))).thenReturn(responseCarDto());
    }

    private void assertCarPackageDto(CarPackageDto dto, Page<Car> page) {
        assertThat(dto).isNotNull();
        assertThat(dto.pageNumber()).isEqualTo(DEFAULT_PAGE);
        assertThat(dto.pageSize()).isEqualTo(DEFAULT_PAGE_SIZE);
        assertThat(dto.totalElements()).isEqualTo(page.getTotalElements());
        assertThat(dto.carsDto()).hasSize(page.getContent().size());
    }

    @Test
    @DisplayName("getCurrentCarByProfileId returns current car of driver")
    void getCurrentCarByProfileId_returnsCurrentCar() {
        DriverProfile driverProfile = createDriverProfile();
        Car currentCar = carEntity();

        when(profileValidationManager.getDriverProfile(DEFAULT_ID)).thenReturn(driverProfile);
        when(carRepo.findByDriverProfileAndIsCurrent(driverProfile, true)).thenReturn(currentCar);

        Car result = readCarService.getCurrentCarByProfileId(DEFAULT_ID);

        assertThat(result).isEqualTo(currentCar);
        verify(carRepo).findByDriverProfileAndIsCurrent(driverProfile, true);
        verify(carValidationManager).checkCarNotNull(currentCar);
    }
}