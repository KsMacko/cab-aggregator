package com.internship.driverservice.config;

import com.internship.driverservice.service.communication.ArtemisProducer;
import com.internship.driverservice.service.communication.FinanceFeignClient;
import com.internship.driverservice.service.communication.KafkaConsumer;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class DisableBroker {

    @Bean
    public ArtemisProducer artemisProducer() {
        return Mockito.mock(ArtemisProducer.class);
    }

    @Bean
    public KafkaConsumer kafkaConsumer() {
        return Mockito.mock(KafkaConsumer.class);
    }

    @Bean
    public FinanceFeignClient financeFeignClient() {
        return Mockito.mock(FinanceFeignClient.class);
    }
}