package com.internship.rideservice.utils;

import com.internship.rideservice.dto.request.RequestPromoCodeDto;
import com.internship.rideservice.dto.response.ResponsePromoCodeDto;
import com.internship.rideservice.dto.transfer.PromoCodeFilterRequest;
import com.internship.rideservice.entity.PromoCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PromoCodeUtil implements UtilConstants {

    public static RequestPromoCodeDto validPromoCodeDto() {
        return RequestPromoCodeDto.builder()
                .promoCode(VALID_PROMO_CODE)
                .discount(VALID_DISCOUNT)
                .validUntil(VALID_PROMO_VALID_UNTIL)
                .build();
    }

    public static ResponsePromoCodeDto responsePromoCodeDto() {
        return ResponsePromoCodeDto.builder()
                .promoCode(VALID_PROMO_CODE)
                .id(VALID_ID)
                .discount(VALID_DISCOUNT)
                .build();
    }

    public static PromoCode promoCodeEntity() {
        return PromoCode.builder()
                .id(VALID_ID)
                .promoCode(VALID_PROMO_CODE)
                .discount(VALID_DISCOUNT)
                .validUntil(LocalDateTime.now().plusMonths(3))
                .build();
    }

    public static PromoCodeFilterRequest validPromoCodeFilterRequest() {
        return PromoCodeFilterRequest.builder()
                .page(DEFAULT_PAGE_NUMBER)
                .size(DEFAULT_PAGE_SIZE)
                .sortBy(PROMO_CODE_SORT_FIELD)
                .order(DEFAULT_ORDER)
                .build();
    }
}