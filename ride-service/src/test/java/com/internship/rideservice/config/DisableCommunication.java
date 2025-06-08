package com.internship.rideservice.config;

import com.internship.rideservice.service.communication.ArtemisConsumer;
import com.internship.rideservice.service.communication.FinanceFeignClient;
import com.internship.rideservice.service.communication.KafkaProducer;
import com.internship.rideservice.service.communication.PassengerFeignClient;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;


@TestConfiguration
public class DisableCommunication {

    @Bean
    public ArtemisConsumer artemisConsumer() {
        return Mockito.mock(ArtemisConsumer.class);
    }


    @Bean
    public KafkaProducer kafkaProducer() {
        return Mockito.mock(KafkaProducer.class);
    }

    @Bean
    public FinanceFeignClient financeFeignClient() {
        return Mockito.mock(FinanceFeignClient.class);
    }

    @Bean
    public PassengerFeignClient passengerFeignClient() {
        return Mockito.mock(PassengerFeignClient.class);
    }
}
