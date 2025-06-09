package com.internship.driverservice.integration;

import com.internship.driverservice.DriverServiceApplication;
import com.internship.driverservice.config.DisableBroker;
import com.internship.driverservice.config.MySQLContainerConfig;
import com.internship.driverservice.config.WireMockConfig;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@AutoConfigureMockMvc
@SpringBootTest(
        classes = DriverServiceApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import({
        MySQLContainerConfig.class,
        DisableBroker.class,
        WireMockConfig.class
})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {

}