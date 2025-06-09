package com.internship.driverservice.e2e;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;

import static com.internship.driverservice.config.JsonFiles.validJsonCarRequest;
import static com.internship.driverservice.config.JsonFiles.validJsonProfileRequest;
import static com.internship.driverservice.util.UtilConstants.BASE_URL;
import static com.internship.driverservice.util.UtilConstants.CAR_BASE_URL;
import static com.internship.driverservice.util.UtilConstants.DRIVER_BASE_URL;
import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.equalTo;

import com.internship.driverservice.config.JsonFiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CarSteps {

    @LocalServerPort
    private int port;

    private Response response;
    private Long driverId;
    private Long carId;

    @Given("I have driver information")
    public void i_have_driver_information() {
    }
    @Given("I have car information")
    public void i_have_car_information() {
    }
    @Given("I have created car")
    public void i_have_created_car() {
    }

    @When("I send a POST request to create profile")
    public void i_send_a_post_request_to_create_profile() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        response = given()
                .contentType("application/json")
                .body(JsonFiles.validJsonProfileRequest)
                .when()
                .post(DRIVER_BASE_URL);

        String location = response.getHeader(HttpHeaders.LOCATION);
        driverId = Long.valueOf(location.replace(BASE_URL +":"+port+ DRIVER_BASE_URL + "/", ""));
    }

    @When("I send a POST request to create car")
    public void i_send_a_post_request_to_create_car() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        String modified = validJsonCarRequest.replace("1234AA7", "4444AA7");
        response = given()
                .contentType("application/json")
                .body(modified)
                .when()
                .post(CAR_BASE_URL);

        carId = response.getBody().jsonPath().getLong("id");
    }

    @When("I send a PATCH request to set created car as current")
    public void i_send_a_patch_request_set_created_car_current_for_driver() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        response = given()
                .contentType("application/json")
                .when()
                .patch(CAR_BASE_URL + "/{id}", carId);
    }

    @Then("the car is marked as current")
    public void the_car_is_marked_as_current() {
        response.then().assertThat().statusCode(200);
        response.then().body("isCurrent", equalTo(true));
    }

    @When("I delete the car")
    public void i_delete_the_car() {
        response = given()
                .when()
                .delete(CAR_BASE_URL + "/{id}", carId);
    }

    @Then("the response should return status code {int}")
    public void the_response_has_status_code(int statusCode) {
        response.then().assertThat().statusCode(statusCode);
    }

}