package com.internship.passengerservice.e2e;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static com.internship.passengerservice.config.JsonFiles.BASE_PASSENGERS;
import static com.internship.passengerservice.config.JsonFiles.validPassengerRequest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PassengerSteps {

    @LocalServerPort
    private int port;

    private Response response;
    private Long passengerId;
    private final Long authorId = 1L;

    @Given("I have a valid passenger profile")
    public void i_have_a_valid_passenger_profile() {
    }

    @When("I send a POST request to create a passenger profile with number {string} and email {string}")
    public void create_passenger_profile(String number, String email) {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        String modified = validPassengerRequest
                .replace("375291234567", number)
                .replace("petr@example.com", email);
        response = given()
                .contentType("application/json")
                .body(modified)
                .when()
                .post(BASE_PASSENGERS);

        passengerId = response.getBody().jsonPath().getLong("profileId");
    }

    @Then("the response should return status code {int}")
    public void check_status_code(int expectedStatusCode) {
        response.then().statusCode(expectedStatusCode);
    }

    @Then("the created profile should match the sent data")
    public void check_created_profile_data() {
        response.then()
                .body("firstName", equalTo("Петр"))
                .body("email", equalTo("petr@example.com"))
                .body("phone", equalTo("375291234567"))
                .body("profileId", notNullValue());
    }

    @Then("I can GET the passenger by ID and receive the same data")
    public void get_passenger_by_id() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        response = given()
                .when()
                .get(BASE_PASSENGERS+"/{id}", passengerId);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("firstName", equalTo("Петр"))
                .body("email", equalTo("petr@example.com"))
                .body("phone", equalTo("375291234567"))
                .body("profileId", equalTo(passengerId.intValue()));
    }

    @When("I delete the passenger profile")
    public void delete_passenger_profile() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        response = given()
                .when()
                .delete(BASE_PASSENGERS+"/{id}", passengerId);
    }

    @Then("GET request for that passenger returns 404")
    public void check_passenger_not_found_after_delete() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        response = given()
                .when()
                .get(BASE_PASSENGERS+"/{id}", passengerId);

        response.then().statusCode(HttpStatus.NOT_FOUND.value());
    }
}