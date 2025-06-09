package com.internship.rideservice.e2e;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static com.internship.rideservice.config.JsonFiles.BASE_FARES;
import static com.internship.rideservice.config.JsonFiles.validFareRequest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FareSteps {

    @LocalServerPort
    private int port;

    private Response response;
    private final String fareType = "ECONOMY";

    @Given("I have a valid fare request")
    public void prepare_fare_request() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @When("I send a POST request to create a fare")
    public void create_fare() {
        response = given()
                .contentType("application/json")
                .body(validFareRequest)
                .when()
                .post(BASE_FARES);
    }

    @Then("the response should return status code {int}")
    public void check_status_code(int expectedStatusCode) {
        response.then().statusCode(expectedStatusCode);
    }

    @Then("the created fare should match the sent data")
    public void check_created_fare_data() {
        response.then()
                .body("type", equalTo(fareType));
    }

    @Then("I can GET the fare by type and receive the same data")
    public void get_fare_by_type() {
        response = given()
                .when()
                .get(BASE_FARES+"/{type}", fareType);
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("type", equalTo(fareType));
    }

    @When("I delete the fare")
    public void delete_fare() {
        response = given()
                .when()
                .delete(BASE_FARES+"/{type}", fareType);
    }

    @Then("GET request for that fare returns 404")
    public void check_fare_not_found_after_delete() {
        response = given()
                .when()
                .get(BASE_FARES+"/{type}", fareType);

        response.then().statusCode(HttpStatus.NOT_FOUND.value());
    }
}