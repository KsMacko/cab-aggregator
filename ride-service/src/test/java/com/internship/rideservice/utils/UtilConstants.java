package com.internship.rideservice.utils;

import java.math.BigDecimal;
import java.util.List;

public interface UtilConstants {
    String VALID_ID = "ident";
    Long VALID_PERSON_ID = 1L;
    String VALID_FARE_TYPE = "ECONOMY";
    BigDecimal VALID_PRICE = new BigDecimal("5.00");
    Integer VALID_INTEGER = 10;
    Byte VALID_DISCOUNT = Byte.valueOf("10");

    String VALID_PROMO_CODE = "PROMO123";
    String VALID_PROMO_VALID_UNTIL = "2025-12-31T23:59:59";

    String VALID_START_LOCATION = "Point A";
    List<String> VALID_END_LOCATIONS = List.of("Point B", "Point C");
    Float VALID_DISTANCE = 15.5f;

    String VALID_RIDE_STATUS = "IN_PROGRESS";
    String VALID_PAYMENT_TYPE = "CASH";

    String PROMO_CODE_SORT_FIELD = "CREATED_AT";
    String RIDE_SORT_FIELD = "DATE";
    String DEFAULT_ORDER = "ASC";
    Integer DEFAULT_PAGE_NUMBER = 1;
    Integer DEFAULT_PAGE_SIZE = 10;
}