package com.internship.rideservice.config;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

@CucumberContextConfiguration
@CucumberOptions(
        features = "classpath:features/e2e",
        glue = "com/internship/rideservice/e2e",
        plugin = {"pretty", "html:target/cucumber-report.html"}
)
@Import({MongoDBContainerConfig.class, WireMockConfig.class})
@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EndToEndRunner {
}
