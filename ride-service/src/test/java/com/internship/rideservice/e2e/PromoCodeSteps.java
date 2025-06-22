package com.internship.rideservice.e2e;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static com.internship.rideservice.config.JsonFiles.BASE_PROMO_CODES;
import static com.internship.rideservice.config.JsonFiles.validPromoCodeRequest;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PromoCodeSteps {

    @LocalServerPort
    private int port;

    private Response response;
    private final String promoCode = "SUM25";
    private String createdCode;
    private String createdPromoCodeId;

    @Given("I have a valid promo code request")
    public void prepare_promo_code_request() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @When("I send a POST request to create a promo code {string}")
    public void create_promo_code(String promoCode) {
        String modified = validPromoCodeRequest.replace("SUM25", promoCode);
        response = given()
                .contentType("application/json")
                .body(modified)
                .when()
                .post(BASE_PROMO_CODES);
        createdPromoCodeId = response.jsonPath().getString("id");
        createdCode = response.getBody().jsonPath().getString("promoCode");
    }

    @Then("The response has status code {int}")
    public void check_status_code(int expectedStatusCode) {
        response.then().statusCode(expectedStatusCode);
    }

    @Then("the created promo code should match the sent data")
    public void check_created_promo_code_data() {
        response.then()
                .body("discount", equalTo(25))
                .body("validUntil", equalTo("2025-12-31T23:59:59"))
                .body("id", notNullValue());
    }

    @Then("I can GET the promo code by code and receive the same data")
    public void get_promo_code_by_code() {
        response = given()
                .when()
                .get(BASE_PROMO_CODES+"/{code}/current", createdCode);

        response.then()
                .statusCode(HttpStatus.OK.value())
                .body("promoCode", equalTo(createdCode))
                .body("discount", equalTo(25));
    }

    @When("I delete the promo code")
    public void delete_promo_code() {
        response = given()
                .when()
                .delete(BASE_PROMO_CODES+"/{id}", createdPromoCodeId);
    }

    @Then("GET request for that promo code returns 404")
    public void check_promo_code_not_found_after_delete() {
        response = given()
                .when()
                .get(BASE_PROMO_CODES+"/{code}/current", createdCode);

        response.then().statusCode(HttpStatus.NOT_FOUND.value());
    }
}