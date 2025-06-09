package com.internship.passengerservice.config;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@CucumberContextConfiguration
@CucumberOptions(
        features = "classpath:features/e2e",
        glue = "com/internship/passengerservice/e2e",
        plugin = {"pretty", "html:target/cucumber-report.html"}
)
@Import({MySQLContainerConfig.class, WireMockConfig.class})
@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EndToEndRunner {
}
