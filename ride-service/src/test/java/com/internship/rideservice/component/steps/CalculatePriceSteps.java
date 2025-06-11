package com.internship.rideservice.component.steps;

import com.internship.rideservice.entity.Fare;
import com.internship.rideservice.entity.PromoCode;
import com.internship.rideservice.entity.Ride;
import com.internship.rideservice.enums.FareType;
import com.internship.rideservice.service.command.CalculatePriceService;
import com.internship.rideservice.util.validators.FareValidationManager;
import com.internship.rideservice.util.validators.PromoCodeValidationManager;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class CalculatePriceSteps {

    @Mock
    private FareValidationManager fareValidationManager;

    @Mock
    private PromoCodeValidationManager promoCodeValidationManager;

    @InjectMocks
    private CalculatePriceService calculatePriceService;

    private static final Ride ride = new Ride();
    private static final Fare fare = new Fare();
    private static final PromoCode promoCode = spy(new PromoCode());
    private static LocalTime startTime;
    private BigDecimal result;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Given("I have a ride with ID {string} with duration {int} minutes and distance {float} km")
    public void i_have_ride_with_id(String rideId, Integer minutes, Float km) {
        ride.setId(rideId);
        ride.setDistance(km);
        startTime = LocalTime.now().minusMinutes(minutes);
        ride.setStartTime(startTime);
        ride.setEndTime(LocalTime.now());
    }
    @Given("waiting time was {int} minutes")
    public void waiting_time_is(int minutes) {
        ride.setStartWaitingTime(startTime.plusMinutes(minutes));
    }

    @Given("fare type is {string} with price per km {float} and per minute {float}")
    public void fare_type_is(String fareType, float pricePerKm, float perMin) {
        FareType type = FareType.valueOf(fareType.toUpperCase());
        ride.setFareType(type);
        fare.setType(type);
        fare.setPricePerKm(BigDecimal.valueOf(pricePerKm));
        fare.setPricePerMin(BigDecimal.valueOf(perMin));
    }
    @Given("fare min price is {float} with {int} minutes for free waiting and payment for waiting is {float} per minute")
    public void fare_type_prices(float minPrice, int freeWaiting, float pricePerMinute) {
        fare.setMinPrice(BigDecimal.valueOf(minPrice));
        fare.setFreeWaiting(freeWaiting);
        fare.setPaidWaitingPrice(BigDecimal.valueOf(pricePerMinute));
        when(fareValidationManager.getFareIfExists(any())).thenReturn(fare);
    }

    @Given("promo code {string} gives {byte}% discount")
    public void promo_code_gives_discount(String code, byte percent) {
        ride.setPromoCode(code);
        promoCode.setDiscount(percent);
        when(promoCodeValidationManager.getCurrentPromoCode(code)).thenReturn(promoCode);
    }

    @Given("promo code is not applied")
    public void promo_code_is_not_applied() {
        ride.setPromoCode(null);
        when(promoCodeValidationManager.getCurrentPromoCode(anyString())).thenReturn(null);
    }

    @When("I recalculate price")
    public void i_recalculate_price() {
        result = calculatePriceService.ReCalculatePrice(ride);
    }

    @Then("total price equals min price plus distance multiply price per km")
    public void total_price_equals_kms_times_price_per_km() {
        BigDecimal expected = calculateExpectedPriceByKm();
        assertThat(result).isEqualTo(expected);
    }

    @Then("total price should be based on time")
    public void total_price_based_on_time() {
        BigDecimal expected = calculateExpectedPriceByMin()
                .add(fare.getMinPrice())
                .setScale(2, RoundingMode.HALF_UP);
        assertThat(result).isEqualTo(expected);
    }

    @Then("total price should be reduced by {byte}%")
    public void total_price_reduced_by(byte percent) {
        BigDecimal expected = calculateExpectedPriceByMin().multiply(BigDecimal.valueOf(1 - percent / 100.0))
                .setScale(2, RoundingMode.HALF_UP)
                .add(fare.getMinPrice());
        assertThat(result).isEqualTo(expected);
    }

    @Then("total price includes {int} minutes of paid waiting time")
    public void total_price_includes_paid_waiting_time(int extraWaitingMin) {
        BigDecimal extra = fare.getPaidWaitingPrice()
                .multiply(BigDecimal.valueOf(extraWaitingMin));
        BigDecimal expected = calculateExpectedPriceByKm()
                .add(extra)
                .setScale(2, RoundingMode.HALF_UP);
        assertThat(result).isEqualTo(expected);
    }

    @Then("final price is time-based plus {int} minutes of paid waiting with {byte}% discount")
    public void final_price_with_time_and_waiting(int extraWaitingMinutes, byte discount) {
        BigDecimal waitingPrice = fare.getPaidWaitingPrice()
                .multiply(BigDecimal.valueOf(extraWaitingMinutes));

        BigDecimal expectedTotal = calculateExpectedPriceByMin()
                .multiply(BigDecimal.valueOf(100-discount)
                        .divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP))
                .add(fare.getMinPrice())
                .add(waitingPrice)
                .setScale(2, RoundingMode.HALF_UP);
        assertThat(result).isEqualTo(expectedTotal);
    }
    private BigDecimal calculateExpectedPriceByKm(){
        return fare.getPricePerKm()
                .multiply(BigDecimal.valueOf(ride.getDistance()))
                .add(fare.getMinPrice())
                .setScale(2, RoundingMode.HALF_UP);
    }
    private BigDecimal calculateExpectedPriceByMin(){
        return fare.getPricePerMin()
                .multiply(BigDecimal.valueOf(Duration.between(ride.getStartTime(), ride.getEndTime()).toMinutes()));
    }
}