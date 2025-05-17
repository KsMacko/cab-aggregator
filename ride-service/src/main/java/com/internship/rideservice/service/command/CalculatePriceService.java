package com.internship.rideservice.service.command;

import com.internship.rideservice.entity.Fare;
import com.internship.rideservice.entity.PromoCode;
import com.internship.rideservice.entity.Ride;
import com.internship.rideservice.util.validators.FareValidationManager;
import com.internship.rideservice.util.validators.PromoCodeValidationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class CalculatePriceService {
    private final FareValidationManager fareValidationManager;
    private final PromoCodeValidationManager promoCodeValidationManager;

    public BigDecimal ReCalculatePrice(Ride ride) {
        Fare fare = fareValidationManager.getFareIfExists(ride.getFareType());
        BigDecimal currentAmount = BigDecimal.valueOf(ride.getDistance()).multiply(fare.getPricePerKm());
        currentAmount = currentAmount.setScale(2, RoundingMode.HALF_UP);
        BigDecimal amountByTime = BigDecimal.valueOf(Duration.between(
                        ride.getStartTime(),
                        ride.getEndTime()
                ).toMinutes())
                .multiply(fare.getPricePerMin());
        BigDecimal maxPrice = currentAmount.max(amountByTime);
        PromoCode promoCode = promoCodeValidationManager.getCurrentPromoCode(ride.getPromoCode());
        BigDecimal totalPrice;
        if (nonNull(promoCode)) {
            totalPrice = maxPrice.multiply(
                    BigDecimal.ONE.subtract(
                            BigDecimal.valueOf(promoCode.getDiscount())
                                    .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)));
        } else {
            totalPrice = maxPrice;
        }
        long waitingTime = Duration.between(ride.getStartWaitingTime(), ride.getStartTime()).toMinutes();
        if (waitingTime > fare.getFreeWaiting()) {
            totalPrice = totalPrice.add(fare.getPaidWaitingPrice()
                    .multiply(BigDecimal.valueOf(waitingTime - fare.getFreeWaiting()))
            );
        }
        return totalPrice;
    }
}
