package com.internship.driverservice.e2e.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.integration.ContractVerifierIntegrationConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;

import static com.internship.driverservice.config.JsonFiles.validJsonCarRequest;
import static com.internship.driverservice.util.UtilConstants.BASE_URL;
import static com.internship.driverservice.util.UtilConstants.CAR_BASE_URL;
import static com.internship.driverservice.util.UtilConstants.DRIVER_BASE_URL;
import static io.restassured.RestAssured.given;

import static com.internship.driverservice.config.JsonFiles.validJsonProfileRequest;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(ContractVerifierIntegrationConfiguration.class)
public class DriverProfileSteps {

    Response response;
    private Long driverId;
    private Long carId;


    @Given("I have driver information")
    public void i_have_driver_information() {
    }
    @Given("I have car information")
    public void i_have_car_information() {
    }

    @When("I send a POST request to create profile")
    public void i_send_a_post_request_to_create_profile() {
        response = given()
                .contentType("application/json")
                .body(validJsonProfileRequest)
                .when()
                .post(DRIVER_BASE_URL);
        String location = response.getHeader(HttpHeaders.LOCATION);
        driverId = Long.valueOf(location.replace(BASE_URL+DRIVER_BASE_URL + "/", ""));
    }

    @When("I send a POST request to create car")
    public void i_send_a_post_request_to_create_car() {
        response = given()
                .contentType("application/json")
                .body(validJsonCarRequest)
                .when()
                .post(CAR_BASE_URL);
        carId = response.getBody().jsonPath().getLong("id");
    }

    @When("I send a PUT request set created car current for driver:")
    public void i_send_a_put_request_set_created_car_current_for_driver() {
        response = given()
                .contentType("application/json")
                .when()
                .put(CAR_BASE_URL, carId);
    }

    @Then("the response status code should be {int}")
    public void checkResponseStatusCode(int statusCode) {
        response.then().statusCode(statusCode);
    }

    @Then("The car should be assigned to driver with ID {long} as current")
    public void car_should_be_assigned_to_driver_with_id_as_current(Long id) {
        response.then()
                .body("driverAssignment.isCurrent", equalTo(true))
                .body("driverAssignment.driver.id", equalTo(driverId));
    }

    @When("Kafka publishes a ride creation event with tariff {string}")
    public void publishRideEvent(String tariff) {
        response = given()
                .queryParam("tariff", tariff)
                .when()
                .post("/api/v1/rides/test-event")
                .then()
                .extract().response();
    }

    @Then("all available drivers with tariff {string} should receive active notifications")
    public void checkActiveNotifications(String tariff) {
        given().queryParam("tariff", tariff)
                .when()
                .get("/api/v1/drivers/notifications")
                .then()
                .body("active", equalTo(true));
    }

    @When("driver with ID {string} accepts the ride notification")
    public void acceptRide(String driverId) {
        response = given()
                .pathParam("driverId", driverId)
                .post("/api/v1/drivers/{driverId}/accept")
                .then()
                .extract().response();
    }

    @Then("notifications of other drivers become inactive")
    public void checkOthersInactive() {
        given().queryParam("exclude", "1")
                .when()
                .get("/api/v1/drivers/notifications")
                .then()
                .body("active", everyItem(equalTo(false)));
    }

    @Then("a message is sent to the Artemis queue about ride acceptance")
    public void checkArtemisMessage() {
        // Implement logic to verify message in Artemis queue
        // e.g., using Embedded ActiveMQ or mock
    }
}