package com.internship.driverservice.component.steps;

import com.internship.driverservice.dto.mapper.CarMapper;
import com.internship.driverservice.dto.request.RequestCarDto;
import com.internship.driverservice.entity.Car;
import com.internship.driverservice.entity.DriverProfile;
import com.internship.driverservice.repo.CarRepo;
import com.internship.driverservice.service.command.CommandCarService;
import com.internship.driverservice.utils.validation.CarValidationManager;
import com.internship.driverservice.utils.validation.ProfileValidationManager;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CarSteps {

    @Mock
    private ProfileValidationManager profileValidationManager;

    @Mock
    private CarValidationManager carValidationManager;

    @Mock
    private CarRepo carRepo;

    @Mock
    private CarMapper carMapper;

    @InjectMocks
    private CommandCarService commandCarService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private String carNumber;
    private Long currentCarId;
    private Long driverId;

    @Given("I have a valid driver with ID {long}")
    public void i_have_valid_driver(Long driverId) {
        this.driverId = driverId;
        DriverProfile profile = mock(DriverProfile.class);
        profile.setProfileId(driverId);
        when(profileValidationManager.getDriverProfile(driverId)).thenReturn(profile);
    }

    @Given("car number {string} is unique")
    public void car_number_is_unique(String carNumber) {
        this.carNumber = carNumber;
        doNothing().when(carValidationManager).validateCarNumberUniqueness(carNumber);
    }

    @When("I add a new car for this driver")
    public void i_add_new_car_for_driver() {
        when(carMapper.handleDto(any(RequestCarDto.class))).thenReturn(
                Car.builder().carNumber(this.carNumber).build()
        );
        commandCarService.addNewCar(RequestCarDto.builder()
                .carNumber(this.carNumber)
                .build());
    }

    @Then("car is saved and linked to driver")
    public void car_is_saved_and_linked_to_driver() {
        verify(carRepo).save(any(Car.class));
    }

    @Given("I have a car with ID {long}")
    public void i_have_car_with_id(Long carId) {
        this.currentCarId = carId;
        Car car = mock(Car.class);
        DriverProfile driverProfile = mock(DriverProfile.class);

        when(car.getId()).thenReturn(carId);
        when(car.getDriverProfile()).thenReturn(driverProfile);
        when(driverProfile.getProfileId()).thenReturn(this.driverId);
        when(carValidationManager.getIfExistsById(carId)).thenReturn(car);
    }

    @When("I delete it")
    public void i_delete_it() {
        commandCarService.deleteCar(this.currentCarId);
    }
    @When("I set it as current")
    public void i_set_it_as_current() {
        Car car = Car.builder()
                .id(this.currentCarId)
                .isCurrent(false)
                .driverProfile(mock(DriverProfile.class))
                .build();
        DriverProfile profile = mock(DriverProfile.class);
        when(profile.getProfileId()).thenReturn(this.driverId);

        when(carValidationManager.getIfExistsById(this.currentCarId)).thenReturn(car);

        commandCarService.setCurrentCar(this.currentCarId);
    }

    @Then("car is removed from database")
    public void car_is_removed_from_db() {
        verify(carRepo).delete(any(Car.class));
    }

    @Then("all other cars become non-current")
    public void all_other_cars_become_non_current() {
        List<Car> cars = mock(List.class);
        when(cars.size()).thenReturn(anyInt());
        when(carRepo.findAllByDriverProfile_ProfileId(this.driverId)).thenReturn(cars);

        commandCarService.setCurrentCar(this.currentCarId);

        verify(carRepo, atLeastOnce()).saveAll(anyList());
    }

    @Then("this car becomes current")
    public void this_car_becomes_current() {
        verify(carRepo, atLeastOnce()).save(argThat(Car::getIsCurrent));
    }
}
