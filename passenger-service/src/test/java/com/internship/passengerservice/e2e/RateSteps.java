package com.internship.passengerservice.e2e;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.HashMap;
import java.util.Map;

import static com.internship.passengerservice.config.JsonFiles.BASE_RATES;
import static com.internship.passengerservice.config.JsonFiles.validRateRequest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RateSteps {

    @LocalServerPort
    private int port;

    private Response response;
    private final Long personId = 1L;
    private Long rateId;

    @Given("I have a valid rate request for passenger")
    public void i_have_valid_rate_request_for_passenger() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @When("I send a POST request to rate passenger")
    public void post_rate_to_passenger() {
        response = given()
                .contentType("application/json")
                .body(validRateRequest)
                .when()
                .post(BASE_RATES+"/author/driver");
    }

    @Then("The response has status code {int}")
    public void check_status_code(int expectedStatusCode) {
        response.then().statusCode(expectedStatusCode);
    }

    @Given("a rate exists for passenger")
    public void create_rate_for_passenger_delete() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        response = given()
                .contentType("application/json")
                .body(validRateRequest)
                .when()
                .post(BASE_RATES+"/author/driver");

        rateId = response.getBody().jsonPath().getLong("id");
    }

    @When("I send a DELETE request to delete rate from passenger")
    public void delete_rate_from_driver() {
        response = given()
                .pathParam("rateId", rateId)
                .pathParam("driverId", personId)
                .when()
                .delete(BASE_RATES+"/{rateId}/author/driver/{driverId}");
    }

    @Given("I have a valid rate request for driver")
    public void i_have_valid_rate_request_for_driver() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @When("I send a POST request to rate driver")
    public void post_rate_to_driver() {
        response = given()
                .contentType("application/json")
                .body(validRateRequest)
                .when()
                .post(BASE_RATES+"/author/passenger");
    }

    @Given("a rate exists for driver")
    public void create_rate_for_delete() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        response = given()
                .contentType("application/json")
                .body(validRateRequest)
                .when()
                .post(BASE_RATES+"/author/passenger");

        rateId = response.getBody().jsonPath().getLong("id");
    }

    @When("I send a DELETE request to delete rate from driver")
    public void delete_rate_from_passenger() {
        response = given()
                .pathParam("rateId", rateId)
                .pathParam("passengerId", personId)
                .when()
                .delete(BASE_RATES+"/{rateId}/author/passenger/{passengerId}");
    }
}