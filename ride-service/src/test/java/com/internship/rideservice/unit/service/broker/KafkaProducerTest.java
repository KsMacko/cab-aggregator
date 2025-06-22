package com.internship.rideservice.unit.service.broker;

import com.internship.commonevents.event.CashConfirmationRequest;
import com.internship.commonevents.event.RideNotificationEvent;
import com.internship.rideservice.service.communication.KafkaProducer;
import com.internship.rideservice.utils.RideUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class KafkaProducerTest {

    @Mock
    private KafkaTemplate<String, CashConfirmationRequest> cashConfirmationKafkaTemplate;

    @Mock
    private KafkaTemplate<String, RideNotificationEvent> rideNotificationKafkaTemplate;

    private KafkaProducer kafkaProducer;

    @BeforeEach
    void setUp() {
        kafkaProducer = spy(new KafkaProducer(cashConfirmationKafkaTemplate, rideNotificationKafkaTemplate));
    }

    @Test
    void sendPaymentByCashConfirmation_shouldSendToCorrectTopic_withValidData() {
        CashConfirmationRequest request = RideUtil.cashConfirmationRequest();

        kafkaProducer.sendPaymentByCashConfirmation(request);
        verify(cashConfirmationKafkaTemplate).send(eq("payment-confirmation"), argThat(req ->
                req.driverId().equals(request.driverId()) &&
                        req.passengerId().equals(request.passengerId()) &&
                        req.rideId().equals(request.rideId()) &&
                        req.amount().equals(request.amount())
        ));
    }
}
