package com.internship.passengerservice.config;

import com.internship.passengerservice.service.communication.DriverFeignClient;
import com.internship.passengerservice.service.communication.RideFeignClient;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class DisableCommunication {
    @Bean
    public DriverFeignClient driverFeignClient() {
        return Mockito.mock(DriverFeignClient.class);
    }

    @Bean
    public RideFeignClient rideFeignClient() {
        return Mockito.mock(RideFeignClient.class);
    }
}