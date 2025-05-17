package com.internship.rideservice.dto.transfer;

import com.internship.rideservice.dto.response.ResponsePromoCodeDto;
import lombok.Builder;

import java.util.List;

@Builder
public record PromoCodePackageDto(
        List<ResponsePromoCodeDto> promoCodeDtoList,
        long totalElements,
        int pageNumber,
        int pageSize,
        int totalPages
) {
}