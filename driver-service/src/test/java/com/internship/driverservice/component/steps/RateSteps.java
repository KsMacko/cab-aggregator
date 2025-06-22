package com.internship.driverservice.component.steps;

import com.internship.commonevents.event.RideParticipantsConfirmation;
import com.internship.driverservice.dto.mapper.RateMapper;
import com.internship.driverservice.dto.request.RequestRateDto;
import com.internship.driverservice.entity.DriverProfile;
import com.internship.driverservice.entity.Rate;
import com.internship.driverservice.repo.RateRepo;
import com.internship.driverservice.service.command.RateService;
import com.internship.driverservice.service.communication.PassengerFeignClient;
import com.internship.driverservice.service.communication.RideFeignClient;
import com.internship.driverservice.utils.validation.ProfileValidationManager;
import com.internship.driverservice.utils.validation.RateValidationManager;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RateSteps {

    @Mock
    private RideFeignClient rideFeignClient;

    @Mock
    private PassengerFeignClient passengerFeignClient;

    @Mock
    private RateRepo rateRepo;

    @Mock
    private RateMapper rateMapper;

    @Mock
    private ProfileValidationManager profileValidationManager;

    @Mock
    private RateValidationManager rateValidationManager;

    @Mock
    DriverProfile driverProfile;

    @InjectMocks
    private RateService rateService;

    private String currentRideId;
    private Long currentPassengerId;
    private Long currentDriverId;
    private RequestRateDto currentRateDto;
    @Mock
    private Rate currentRate;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    public void setUpEach() {
        when(currentRate.getDriver()).thenReturn(driverProfile);
    }

    @Given("I have a ride with ID {string}")
    public void i_have_ride_with_id(String rideId) {
        this.currentRideId = rideId;
        RideParticipantsConfirmation confirmation = mock(RideParticipantsConfirmation.class);

        when(rideFeignClient.checkParticipants(rideId)).thenReturn(confirmation);
    }

    @Given("I am a passenger with ID {long}")
    public void i_am_a_passenger(Long passengerId) {
        currentPassengerId = passengerId;
        when(currentRate.getAuthorId()).thenReturn(passengerId);
    }

    @Given("I am a driver with ID {long}")
    public void i_am_a_driver(Long currentDriverId) {
        currentRate.setAuthorId(currentDriverId);
        this.currentDriverId = currentDriverId;
    }

    @Given("I have rate with ID {long}")
    public void i_have_rate_for_driver(Long rateId) {
        DriverProfile driverProfile = mock(DriverProfile.class);
        when(profileValidationManager.getDriverProfile(this.currentDriverId)).thenReturn(driverProfile);

        when(currentRate.getId()).thenReturn(rateId);
        when(currentRate.getAuthorId()).thenReturn(currentPassengerId);
    }

    @Given("I have a rate from passenger with ID {long} to driver with ID {long}")
    public void i_have_rate_from_passenger_to_driver(Long passengerId, Long driverId) {
        this.currentPassengerId = passengerId;
        this.currentDriverId = driverId;

        DriverProfile driverProfile = mock(DriverProfile.class);
        when(profileValidationManager.getDriverProfile(driverId)).thenReturn(driverProfile);

        when(driverProfile.getProfileId()).thenReturn(driverId);

        RequestRateDto dto = RequestRateDto.builder()
                .recipientId(driverId)
                .authorId(passengerId)
                .rideId(currentRideId)
                .build();

        this.currentRateDto = dto;
        when(currentRate.getDriver()).thenReturn(driverProfile);
        when(currentRate.getAuthorId()).thenReturn(passengerId);
        when(rateMapper.handleDto(dto)).thenReturn(currentRate);
    }

    @Given("I have a rate to passenger with ID {long} from driver with ID {long}")
    public void i_have_rate_to_passenger_from_driver(Long passengerId, Long driverId) {
        this.currentPassengerId = passengerId;
        this.currentDriverId = driverId;

        DriverProfile driverProfile = mock(DriverProfile.class);
        when(profileValidationManager.getDriverProfile(driverId)).thenReturn(driverProfile);

        RequestRateDto dto = RequestRateDto.builder()
                .recipientId(passengerId)
                .authorId(driverId)
                .rideId(currentRideId != null ? currentRideId : "ride-123")
                .value(5)
                .build();

        this.currentRateDto = dto;
    }

    @When("I get the rate")
    public void i_get_the_rate() {
        rateService.setNewRate(currentRateDto);
    }

    @When("I send the rate to passenger")
    public void i_send_the_rate_to_passenger() {
        rateService.setRateToPassenger(currentRateDto);
    }

    @When("I get the rate from passenger")
    public void i_send_the_rate_to_driver() {
        rateService.setNewRate(currentRateDto);
    }

    @When("I delete this rate from driver")
    public void i_delete_this_rate_from_driver() {
        rateService.deleteRate(currentRate.getAuthorId(), currentRate.getId());
    }

    @When("I delete this rate from passenger")
    public void i_delete_this_rate_from_passenger() {
        rateService.deleteRateFromPassenger(currentRate.getAuthorId(), currentRate.getId());
    }

    @Then("rate should be saved for driver")
    public void rate_should_be_saved_for_driver() {
        verify(rateRepo).save(argThat(r -> r.getDriver().getProfileId().equals(currentDriverId)));
    }

    @Then("ride service confirms participants")
    public void ride_service_confirms_participants() {
        verify(rideFeignClient).checkParticipants(currentRideId);
    }

    @Then("rate is saved to database")
    public void rate_is_saved_to_database() {
        verify(rateRepo).save(any(Rate.class));
    }

    @Then("rate should be send to passenger service")
    public void rate_should_be_sent_to_passenger_service() {
        verify(passengerFeignClient).setRateToPassenger(currentRateDto);
    }

    @Then("author is checked by request to passenger service")
    public void author_is_checked_by_request_to_passenger_service() {
        verify(rateValidationManager).checkRateAuthor(currentRate.getAuthorId(), currentRate.getId());
    }

    @Then("rate is removed from DB")
    public void rate_is_removed_from_db() {
        verify(rateRepo).deleteById(currentRate.getId());
    }

    @Then("the request is sent to passenger service")
    public void request_is_sent_to_passenger_service() {
        verify(passengerFeignClient).deleteRateFromPassenger(currentRate.getAuthorId(), currentRate.getId());
    }
}