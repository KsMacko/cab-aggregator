package com.internship.rideservice.unit.mappers;

import com.internship.rideservice.dto.mapper.PromoCodeMapper;
import com.internship.rideservice.dto.request.RequestPromoCodeDto;
import com.internship.rideservice.dto.response.ResponsePromoCodeDto;
import com.internship.rideservice.entity.PromoCode;
import com.internship.rideservice.utils.PromoCodeUtil;
import com.internship.rideservice.utils.UtilConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PromoCodeMapperTest implements UtilConstants {

    private static final PromoCodeMapper promoCodeMapper = Mappers.getMapper(PromoCodeMapper.class);

    @Mock
    private PromoCode code;

    @Test
    void handleDto_shouldMapToEntity_whenValidRequest() {
        RequestPromoCodeDto dto = PromoCodeUtil.validPromoCodeDto();

        PromoCode result = promoCodeMapper.handleDto(dto);

        assertThat(result).isNotNull();
        assertThat(result.getPromoCode()).isEqualTo(dto.promoCode());
        assertThat(result.getDiscount()).isEqualTo(dto.discount());
        assertThat(result.getValidUntil().truncatedTo(ChronoUnit.SECONDS))
                .isEqualTo(LocalDateTime.parse(dto.validUntil(), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getId()).isNull();
    }

    @Test
    void handleEntity_shouldMapToResponseDto_whenValidEntity() {
        PromoCode entity = PromoCodeUtil.promoCodeEntity();

        ResponsePromoCodeDto result = promoCodeMapper.handleEntity(entity);

        assertThat(result).isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "validUntil")
                .isEqualTo(PromoCodeUtil.responsePromoCodeDto());
        assertThat(result.validUntil()).isEqualTo(entity.getValidUntil());
    }

}