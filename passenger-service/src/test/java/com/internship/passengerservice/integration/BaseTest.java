package com.internship.passengerservice.integration;

import com.internship.passengerservice.PassengerServiceApplication;
import com.internship.passengerservice.config.DisableCommunication;
import com.internship.passengerservice.config.MySQLContainerConfig;
import com.internship.passengerservice.config.WireMockConfig;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@AutoConfigureMockMvc
@SpringBootTest(
        classes = PassengerServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import({
        MySQLContainerConfig.class,
        DisableCommunication.class,
        WireMockConfig.class
})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {

}