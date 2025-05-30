package com.internship.passengerservice.config;

public interface JsonFiles {

    String BASE_PASSENGERS = "/api/v1/passengers";
    String BASE_RATES = "/api/v1/rates";

    String VALID_PASSENGER_REQUEST = "/json/passenger/valid-passenger-request.json";
    String INVALID_PASSENGER_REQUEST = "/json/passenger/invalid-passenger-request.json";
    String VALID_PASSENGER_FILTER_REQUEST = "/json/passenger/valid-passenger-filter-request.json";
    String VALID_RATE_REQUEST = "/json/rate/create-rate-request.json";

    String validPassengerRequest = JsonFileReader.readJsonFile(VALID_PASSENGER_REQUEST);
    String invalidPassengerRequest = JsonFileReader.readJsonFile(INVALID_PASSENGER_REQUEST);
    String validPassengerFilterRequest = JsonFileReader.readJsonFile(VALID_PASSENGER_FILTER_REQUEST);

    String validRateRequest = JsonFileReader.readJsonFile(VALID_RATE_REQUEST);

}