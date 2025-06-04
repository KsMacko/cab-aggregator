package com.internship.driverservice.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class WireMockConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WireMockServer financeWireMockServer() {
        System.out.println("Starting WireMock on port 8084");
        return new WireMockServer(WireMockConfiguration.options()
                .port(8084)
                .usingFilesUnderClasspath("stub"));
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WireMockServer passengerWireMockServer() {
        return new WireMockServer(WireMockConfiguration.options()
                .port(8081)
                .usingFilesUnderClasspath("stub"));
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public WireMockServer rideWireMockServer() {
        return new WireMockServer(WireMockConfiguration.options()
                .port(8083)
                .usingFilesUnderClasspath("stub"));
    }

//    @Bean
//    public FinanceWireMockStubService financeStubService(
//            @Qualifier("financeWireMockServer") WireMockServer server) {
//        return new FinanceWireMockStubService(server);
//    }
//
//    @Bean
//    public PassengerWireMockStubService passengerStubService(
//            @Qualifier("passengerWireMockServer") WireMockServer server) {
//        return new PassengerWireMockStubService(server);
//    }
//
//    @Bean
//    public RideWireMockStubService rideStubService(
//            @Qualifier("rideWireMockServer") WireMockServer server) {
//        return new RideWireMockStubService(server);
//    }
}