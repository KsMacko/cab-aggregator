package com.internship.rideservice.integration.broker;

import com.internship.commonevents.event.CashConfirmationRequest;
import lombok.Getter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Getter
public class KafkaConsumerForTest {

    private CashConfirmationRequest lastRequest;

    @KafkaListener(topics = "payment-confirmation")
    public void receivePaymentByCashConfirmation(CashConfirmationRequest request) {
        this.lastRequest = request;
    }
}