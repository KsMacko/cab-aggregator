package com.internship.rideservice.unit.service;

import com.internship.rideservice.entity.Fare;
import com.internship.rideservice.entity.PromoCode;
import com.internship.rideservice.entity.Ride;
import com.internship.rideservice.service.command.CalculatePriceService;
import com.internship.rideservice.util.validators.FareValidationManager;
import com.internship.rideservice.util.validators.PromoCodeValidationManager;
import com.internship.rideservice.utils.FareUtil;
import com.internship.rideservice.utils.RideUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalculatePriceServiceTest {

    @InjectMocks
    private CalculatePriceService calculatePriceService;

    @Mock
    private FareValidationManager fareValidationManager;

    @Mock
    private PromoCodeValidationManager promoCodeValidationManager;

    @Test
    void reCalculatePrice_shouldReturnPriceBasedOnDistance() {
        Ride ride = RideUtil.rideEntity();

        Fare fare = FareUtil.fareEntity();

        when(fareValidationManager.getFareIfExists(ride.getFareType())).thenReturn(fare);
        when(promoCodeValidationManager.getCurrentPromoCode(ride.getPromoCode())).thenReturn(null);

        BigDecimal result = calculatePriceService.ReCalculatePrice(ride);
        BigDecimal expected = calculateExpectedPrice(ride, fare, null);

        assertThat(result).isEqualByComparingTo(expected);
    }

    private BigDecimal calculateExpectedPrice(Ride ride, Fare fare, PromoCode promoCode) {
        BigDecimal distancePrice = BigDecimal.valueOf(ride.getDistance()).multiply(fare.getPricePerKm());
        BigDecimal timePrice = BigDecimal.valueOf(Duration.between(ride.getStartTime(), ride.getEndTime()).toMinutes())
                .multiply(fare.getPricePerMin());

        BigDecimal maxPrice = distancePrice.max(timePrice);

        if (promoCode != null) {
            maxPrice = maxPrice.multiply(
                    BigDecimal.ONE.subtract(
                            BigDecimal.valueOf(promoCode.getDiscount()).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
                    )
            );
        }

        long waitingTime = Duration.between(ride.getStartWaitingTime(), ride.getStartTime()).toMinutes();
        if (waitingTime > fare.getFreeWaiting()) {
            maxPrice = maxPrice.add(
                    fare.getPaidWaitingPrice()
                            .multiply(BigDecimal.valueOf(waitingTime - fare.getFreeWaiting()))
            );
        }

        return maxPrice.setScale(2, RoundingMode.HALF_UP);
    }
}