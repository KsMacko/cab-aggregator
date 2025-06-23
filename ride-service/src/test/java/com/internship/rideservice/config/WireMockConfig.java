package com.internship.rideservice.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class WireMockConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WireMockServer passengerWireMockServer() {
        return new WireMockServer(WireMockConfiguration.options()
                .port(8888)
                .usingFilesUnderClasspath("stub"));
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WireMockServer financeWireMockServer() {
        return new WireMockServer(WireMockConfiguration.options()
                .port(7777)
                .usingFilesUnderClasspath("stub"));
    }
}

