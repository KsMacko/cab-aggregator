package com.internship.rideservice.unit.mappers;

import com.internship.rideservice.dto.mapper.PromoCodeMapper;
import com.internship.rideservice.dto.response.ResponsePromoCodeDto;
import com.internship.rideservice.entity.PromoCode;
import com.internship.rideservice.utils.UtilConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.internship.rideservice.utils.PromoCodeUtil.promoCodeEntity;
import static com.internship.rideservice.utils.PromoCodeUtil.responsePromoCodeDto;
import static com.internship.rideservice.utils.PromoCodeUtil.validPromoCodeDto;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PromoCodeMapperTest implements UtilConstants {

    private static final PromoCodeMapper promoCodeMapper = Mappers.getMapper(PromoCodeMapper.class);

    @Test
    void handleDto_shouldMapToEntity_whenValidRequest() {
        PromoCode result = promoCodeMapper.handleDto(validPromoCodeDto());
        PromoCode expectedPromoCode = promoCodeEntity();

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "validUntil")
                .isEqualTo(expectedPromoCode);
    }

    @Test
    void handleEntity_shouldMapToResponseDto_whenValidEntity() {
        ResponsePromoCodeDto result = promoCodeMapper.handleEntity(promoCodeEntity());
        ResponsePromoCodeDto expectedResponse = responsePromoCodeDto();

        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

}