package com.internship.driverservice.config;

import io.cucumber.java.en_old.Ac;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.utility.DockerImageName;


@CucumberContextConfiguration
@CucumberOptions(
        features = "classpath:features/e2e",
        glue = "com/internship/driverservice/e2e",
        plugin = {"pretty", "html:target/cucumber-report.html"}
)
@Import({MySQLContainerConfig.class, WireMockConfig.class})
@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EndToEndTestRunner {
    @Container
    static ConfluentKafkaContainer kafka = new ConfluentKafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.1"));
    static {
        kafka.start();
    }
}