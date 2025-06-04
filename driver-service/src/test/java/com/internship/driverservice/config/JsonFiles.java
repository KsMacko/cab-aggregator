package com.internship.driverservice.config;

public interface JsonFiles {

    String VALID_CAR_REQUEST = "/json/car/valid-car-request.json";
    String INVALID_CAR_REQUEST = "/json/car/invalid-car-request.json";
    String VALID_PROFILE_REQUEST = "/json/profile/valid-profile-request.json";
    String VALID_CAR_FILTER_REQUEST = "/json/car/valid-car-filter-request.json";
    String VALID_RATE_REQUEST = "/json/rate/valid-rate-to-driver-request.json";
    String INVALID_PROFILE_REQUEST = "/json/profile/invalid-profile-request.json";
    String VALID_PROFILE_FOR_UPDATE_REQUEST = "/json/profile/valid-profile-update-request.json";
    String VALID_PROFILE_FILTER_REQUEST = "/json/profile/valid-profile-filter-request.json";

    String validJsonProfileRequest = JsonFileReader.readJsonFile(VALID_PROFILE_REQUEST);
    String invalidJsonProfileRequest = JsonFileReader.readJsonFile(INVALID_PROFILE_REQUEST);
    String validJsonProfileForUpdateRequest = JsonFileReader.readJsonFile(VALID_PROFILE_FOR_UPDATE_REQUEST);
    String validJsonProfileFilterRequest = JsonFileReader.readJsonFile(VALID_PROFILE_FILTER_REQUEST);

    String validJsonRateRequest = JsonFileReader.readJsonFile(VALID_RATE_REQUEST);

    String validJsonCarRequest = JsonFileReader.readJsonFile(VALID_CAR_REQUEST);
    String invalidJsonCarRequest= JsonFileReader.readJsonFile(INVALID_CAR_REQUEST);
    String validJsonCarFilterRequest = JsonFileReader.readJsonFile(VALID_CAR_FILTER_REQUEST);
}
