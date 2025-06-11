package com.internship.rideservice.component.steps;

import com.internship.commonevents.event.CashConfirmationRequest;
import com.internship.commonevents.event.ChangeRideStatusEvent;
import com.internship.commonevents.event.ConfirmedPaymentRequest;
import com.internship.rideservice.dto.mapper.RideMapper;
import com.internship.rideservice.entity.Ride;
import com.internship.rideservice.enums.PaymentType;
import com.internship.rideservice.repo.RideRepo;
import com.internship.rideservice.service.command.CalculatePriceService;
import com.internship.rideservice.service.command.CommandRideService;
import com.internship.rideservice.service.communication.FinanceFeignClient;
import com.internship.rideservice.service.communication.KafkaProducer;
import com.internship.rideservice.util.validators.PromoCodeValidationManager;
import com.internship.rideservice.util.validators.RideValidationManager;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChangeRideStatusSteps {

    @Mock
    private RideRepo rideRepo;

    @Mock
    private CalculatePriceService calculatePriceService;

    @Mock
    private RideMapper rideMapper;

    @Mock
    private RideValidationManager rideValidationManager;

    @Mock
    private KafkaProducer kafkaProducer;

    @Mock
    private FinanceFeignClient financeFeignClient;

    @Mock
    private PromoCodeValidationManager promoCodeValidationManager;

    @InjectMocks
    private CommandRideService commandRideService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private static final Ride ride = spy(new Ride());
    private final ArgumentCaptor<Ride> rideCaptor = ArgumentCaptor.forClass(Ride.class);


    @Given("I have a ride with ID {string}")
    public void i_have_ride_with_id(String rideId) {
        ride.setId(rideId);

        when(rideValidationManager.getRideByIdIfExists(rideId)).thenReturn(ride);
        when(calculatePriceService.ReCalculatePrice(any(Ride.class)))
                .thenReturn(BigDecimal.valueOf(45.0));
    }

    @Given("I have a ride with ID {string} and payment type {string}")
    public void i_have_ride_with_id_and_payment_type(String rideId, String paymentTypeStr) {
        PaymentType paymentType = PaymentType.valueOf(paymentTypeStr);
        ride.setPaymentType(paymentType);

        when(rideValidationManager.getRideByIdIfExists(rideId)).thenReturn(ride);
        when(rideRepo.findById(rideId)).thenReturn(Optional.of(ride));
    }

    @When("I change status to {word} with driver ID {long}")
    public void i_change_status_to_with_driver_id(String status, long driverId) {
        ride.setDriverId(driverId);
        commandRideService.changeRideStatus(
                new ChangeRideStatusEvent(ride.getId(), driverId, status)
        );
    }

    @When("I change status to {word}")
    public void i_change_status_to(String statusStr) {
        commandRideService.changeRideStatus(
                new ChangeRideStatusEvent(ride.getId(), ride.getDriverId(), statusStr)
        );
    }

    @Then("ride should be updated with start waiting time")
    public void ride_should_be_updated_with_start_waiting_time() {
        verify(rideValidationManager).getRideByIdIfExists(eq(ride.getId()));
        verify(rideRepo).save(any(Ride.class));
    }

    @Then("price is recalculated and saved")
    public void price_is_recalculated_and_saved() {
        verify(calculatePriceService).ReCalculatePrice(argThat(r -> r.getId().equals(ride.getId())));
        verify(rideRepo).save(rideCaptor.capture());

        assertThat(rideCaptor.getValue().getPrice()).isNotNull();
        assertThat(rideCaptor.getValue().getPrice()).isNotEqualTo(BigDecimal.ZERO);
    }

    @Then("ride should have driver ID {long}")
    public void ride_should_have_driver_id(long expectedDriverId) {
        verify(rideValidationManager).getRideByIdIfExists(eq(ride.getId()));
        verify(rideRepo).save(rideCaptor.capture());

        assertThat(rideCaptor.getValue().getDriverId()).isEqualTo(expectedDriverId);
    }

    @Then("{word} should be called once")
    public void method_should_be_called_once(String methodName){
        switch (methodName) {
            case "financeFeignClient.createCardPayment":
                verify(financeFeignClient).createCardPayment(any(ConfirmedPaymentRequest.class));
                break;
            case "kafkaProducer.sendPaymentByCashConfirmation":
                verify(kafkaProducer).sendPaymentByCashConfirmation(any(CashConfirmationRequest.class));
                break;
            default:
                throw new IllegalArgumentException("Unknown dependency: " + methodName);
        }
    }
    @Then("validation manager should be called once")
    public void validation_manager_should_be_called_once() {
        verify(rideValidationManager).getRideByIdIfExists(eq(ride.getId()));
    }
}