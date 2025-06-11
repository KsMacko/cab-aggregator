package com.internship.driverservice.util;

import java.math.BigDecimal;

import static com.internship.driverservice.enums.FieldFilter.FIRST_NAME;
import static com.internship.driverservice.enums.OrderDirection.ASC;

public interface UtilConstants {
    String BASE_URL = "http://localhost";
    String DRIVER_BASE_URL = "/api/v1/drivers";
    String CAR_BASE_URL = "/api/v1/drivers/cars";
    String NOTIFICATION_BASE_URL = "/api/v1/drivers/notifications";
    String RATE_BASE_URL = "/api/v1/rates";


    String DEFAULT_STR = "Иван";
    String DEFAULT_FARE_TYPE = "ECONOMY";
    String DEFAULT_PHONE = "375336666666";
    Long DEFAULT_ID = 1L;
    Integer DEFAULT_RATE = 5;
    String DEFAULT_DRIVER_STATUS = "FREE";
    String DEFAULT_FARE = "COMFORT";
    String UPDATED_STR = "Петр";
    String UPDATED_FARE_TYPE = "COMFORT";
    String UPDATED_PHONE = "375337777777";

    String DEFAULT_CAR_NUMBER = "A123AA777";
    String DEFAULT_BRAND = "Toyota";
    String DEFAULT_COLOR = "Черный";
    Boolean DEFAULT_IS_CURRENT = true;
    String UPDATED_CAR_NUMBER = "B456BB777";
    String UPDATED_BRAND = "Honda";
    String UPDATED_COLOR = "Белый";
    Boolean UPDATED_IS_CURRENT = false;

    String DEFAULT_STR_ID = "ride123";
    BigDecimal DEFAULT_AMOUNT = new BigDecimal("10.55");
    String DEFAULT_NOTIFICATION_STATUS = "ACCEPTED";

    int DEFAULT_PAGE = 0;
    int DEFAULT_PAGE_SIZE = 10;
    String DEFAULT_SORT_FIELD = FIRST_NAME.toString();
    String DEFAULT_SORT_DIRECTION = ASC.toString();

}
