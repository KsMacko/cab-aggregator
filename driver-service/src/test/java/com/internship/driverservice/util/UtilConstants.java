package com.internship.driverservice.util;

import com.internship.driverservice.enums.notification.NotificationActivity;

import java.math.BigDecimal;

import static com.internship.driverservice.enums.FieldFilter.FIRST_NAME;

public interface UtilConstants {
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
    String DEFAULT_NOTIFICATION_STATUS = "NON_VIEWED";
    NotificationActivity DEFAULT_ACTIVITY = NotificationActivity.ACTIVE;

    int DEFAULT_PAGE = 0;
    int DEFAULT_PAGE_SIZE = 10;
    String DEFAULT_SORT_FIELD = FIRST_NAME.toString();

}