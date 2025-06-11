package com.internship.rideservice.integration;

import com.internship.rideservice.RideServiceApplication;
import com.internship.rideservice.config.DisableCommunication;
import com.internship.rideservice.config.MongoDBContainerConfig;
import com.internship.rideservice.config.WireMockConfig;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@AutoConfigureMockMvc
@SpringBootTest(
        classes = RideServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import({
        MongoDBContainerConfig.class,
        DisableCommunication.class,
        WireMockConfig.class
})
public abstract class BaseTest {

}