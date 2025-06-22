package com.internship.rideservice.integration.broker;

import com.internship.commonevents.event.CashConfirmationRequest;
import com.internship.rideservice.service.communication.KafkaProducer;
import com.internship.rideservice.utils.RideUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@EmbeddedKafka(partitions = 1,
        topics = {
                "payment-confirmation",
                "ride-creation"
        })
@DirtiesContext
@ActiveProfiles("test")
public class KafkaProducerIT {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private KafkaConsumerForTest kafkaTestConsumer;

    @Test
    void sendPaymentByCashConfirmation_shouldProduceMessageToKafkaTopic() {
        CashConfirmationRequest request = RideUtil.cashConfirmationRequest();

        kafkaProducer.sendPaymentByCashConfirmation(request);

        await().atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> {
                    CashConfirmationRequest received = kafkaTestConsumer.getLastRequest();
                    assertThat(received).isNotNull();
                    assertThat(received.rideId()).isEqualTo(request.rideId());
                    assertThat(received.amount()).isEqualTo(request.amount());
                });
    }
}