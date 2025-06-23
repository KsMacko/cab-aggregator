package com.internship.apigateway.controller;

import com.internship.apigateway.dto.FallbackDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @RequestMapping("/fallback/driver-service")
    public ResponseEntity<FallbackDto> driverServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE.value())
                .body((new FallbackDto("Driver Service is down. Using fallback")));
    }

    @RequestMapping("/fallback/finance-service")
    public ResponseEntity<FallbackDto> financeServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE.value())
                .body((new FallbackDto("Finance Service is down. Using fallback")));
    }

    @RequestMapping("/fallback/passenger-service")
    public ResponseEntity<FallbackDto> passengerServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE.value())
                .body((new FallbackDto("Passenger Service is down. Using fallback")));
    }

    @RequestMapping("/fallback/ride-service")
    public ResponseEntity<FallbackDto> rideServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE.value())
                .body((new FallbackDto("Ride Service is down. Using fallback")));
    }

    @RequestMapping("/fallback/default")
    public ResponseEntity<FallbackDto> defaultFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE.value())
                .body((new FallbackDto("Default Fallback")));
    }
}