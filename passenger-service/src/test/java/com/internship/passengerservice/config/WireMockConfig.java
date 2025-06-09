package com.internship.passengerservice.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class WireMockConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WireMockServer driverWireMockServer() {
        return new WireMockServer(WireMockConfiguration.options()
                .port(8082)
                .usingFilesUnderClasspath("stub"));
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WireMockServer rideWireMockServer() {
        return new WireMockServer(WireMockConfiguration.options()
                .port(8083)
                .usingFilesUnderClasspath("stub"));
    }
}