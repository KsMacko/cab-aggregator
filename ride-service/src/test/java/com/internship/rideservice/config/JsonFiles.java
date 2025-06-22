package com.internship.rideservice.config;

public interface JsonFiles {
    String BASE_URL = "http://localhost";
    String BASE_RIDES = "/api/v1/rides";
    String BASE_FARES = "/api/v1/fares";
    String BASE_PROMO_CODES = "/api/v1/promo-codes";

    String VALID_FARE_REQUEST = "/json/fare/valid-fare-request.json";
    String INVALID_FARE_REQUEST = "/json/fare/invalid-fare-request.json";
    String VALID_PROMO_CODE_REQUEST = "/json/promo/valid-promo-code-request.json";
    String INVALID_PROMO_CODE_REQUEST = "/json/promo/invalid-promo-code-request.json";
    String VALID_PROMO_CODE_FILTER_REQUEST = "/json/promo/valid-promo-code-filter-request.json";
    String VALID_RIDE_REQUEST = "/json/ride/valid-ride-request.json";
    String INVALID_RIDE_REQUEST = "/json/ride/invalid-ride-request.json";
    String VALID_RIDE_FILTER_REQUEST = "/json/ride/valid-ride-filter-request.json";

    String validFareRequest = JsonFileReader.readJsonFile(VALID_FARE_REQUEST);
    String invalidFareRequest = JsonFileReader.readJsonFile(INVALID_FARE_REQUEST);
    String validPromoCodeRequest = JsonFileReader.readJsonFile(VALID_PROMO_CODE_REQUEST);
    String invalidPromoCodeRequest = JsonFileReader.readJsonFile(INVALID_PROMO_CODE_REQUEST);
    String validRideRequest = JsonFileReader.readJsonFile(VALID_RIDE_REQUEST);
    String invalidRideRequest = JsonFileReader.readJsonFile(INVALID_RIDE_REQUEST);
    String validRideFilterRequest = JsonFileReader.readJsonFile(VALID_RIDE_FILTER_REQUEST);
    String validPromoCodeFilterRequest = JsonFileReader.readJsonFile(VALID_PROMO_CODE_FILTER_REQUEST);
}