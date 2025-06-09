package com.internship.driverservice.e2e;


import com.internship.driverservice.config.DisableBroker;
import com.internship.driverservice.config.MySQLContainerConfig;
import com.internship.driverservice.config.WireMockConfig;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.internship.driverservice.config.JsonFiles.validJsonProfileRequest;
import static com.internship.driverservice.util.UtilConstants.BASE_URL;
import static com.internship.driverservice.util.UtilConstants.DRIVER_BASE_URL;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({DisableBroker.class, MySQLContainerConfig.class, WireMockConfig.class})
public class DriverProfileSteps {

    @LocalServerPort
    private int port;

    private Response response;
    private Long driverId;

    @Given("A driver profile exists")
    public void a_driver_profile_exists() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        String modified = validJsonProfileRequest.replace("375291234567", "375335555555");
        response = given()
                .contentType("application/json")
                .body(modified)
                .when()
                .post(DRIVER_BASE_URL);

        String location = response.getHeader(HttpHeaders.LOCATION);
        driverId = Long.valueOf(location.replace(BASE_URL+":"+port + DRIVER_BASE_URL + "/", ""));
    }

    @Given("I have driver info")
    public void i_have_driver_info() {

    }

    @When("I send a POST request to create a driver profile")
    public void i_send_a_post_request_to_create_a_driver_profile() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        response = given()
                .contentType("application/json")
                .body(validJsonProfileRequest)
                .when()
                .post(DRIVER_BASE_URL);
    }

    @Then("the created profile should match the sent data")
    public void the_created_profile_should_match_the_sent_data() {
        response.then()
                .body("profileId", notNullValue())
                .body("firstName", equalTo("Иван"))
                .body("lastName", equalTo("Петров"))
                .body("fareType", equalTo("COMFORT"))
                .body("phone", equalTo("375291234567"));
    }

    @When("I delete the driver profile")
    public void delete_profile() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        response = given()
                .when()
                .delete(DRIVER_BASE_URL + "/{id}", driverId);
    }

    @Then("GET request for that driver returns {int}")
    public void get_profile_returns(int expectedStatus) {
        response = given()
                .when()
                .get(DRIVER_BASE_URL + "/{id}", driverId);

        response.then().statusCode(expectedStatus);
    }
    @Then("The response has status code {int}")
    public void the_response_has_status_code(int statusCode) {
        response.then().assertThat().statusCode(statusCode);
    }


}