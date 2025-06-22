package com.internship.rideservice.unit.service;

import com.internship.rideservice.dto.mapper.PromoCodeMapper;
import com.internship.rideservice.dto.request.RequestPromoCodeDto;
import com.internship.rideservice.dto.response.ResponsePromoCodeDto;
import com.internship.rideservice.dto.transfer.PromoCodeFilterRequest;
import com.internship.rideservice.dto.transfer.PromoCodePackageDto;
import com.internship.rideservice.entity.PromoCode;
import com.internship.rideservice.repo.PromoCodeRepo;
import com.internship.rideservice.service.command.CommandPromoCodeService;
import com.internship.rideservice.service.query.ReadPromoCodeService;
import com.internship.rideservice.util.validators.PromoCodeValidationManager;
import com.internship.rideservice.utils.PromoCodeUtil;
import com.internship.rideservice.utils.UtilConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PromoCodeServiceTest implements UtilConstants {

    @Mock
    private PromoCodeRepo promoCodeRepo;

    @Mock
    private PromoCodeValidationManager validationManager;

    @Mock
    private PromoCodeMapper promoCodeMapper;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private CommandPromoCodeService commandPromoCodeService;

    @InjectMocks
    private ReadPromoCodeService readPromoCodeService;

    private static final ResponsePromoCodeDto dto = PromoCodeUtil.responsePromoCodeDto();
    private static final PromoCode promoCode = PromoCodeUtil.promoCodeEntity();

    @Test
    void createPromoCode_shouldSave_whenValidDto() {
        RequestPromoCodeDto dto = PromoCodeUtil.validPromoCodeDto();


        when(promoCodeMapper.handleDto(dto)).thenReturn(promoCode);
        doNothing().when(validationManager).checkForCreationPromoCodeValidity(dto.promoCode());

        PromoCode result = commandPromoCodeService.createPromoCode(dto);

        verify(promoCodeRepo).save(promoCode);
    }

    @Test
    void deletePromoCode_shouldCallDelete_whenExistsById() {

        doNothing().when(validationManager).checkIfExistsById(VALID_ID);

        commandPromoCodeService.deletePromoCode(VALID_ID);

        verify(promoCodeRepo).deleteById(VALID_ID);
    }

    @Test
    void getFilteredPromoCodes_shouldReturnPage_withPromoCodes() {
        PromoCodeFilterRequest filter = PromoCodeUtil.validPromoCodeFilterRequest();

        List<PromoCode> promoCodes = List.of(PromoCodeUtil.promoCodeEntity());
        when(mongoTemplate.find(any(Query.class), eq(PromoCode.class))).thenReturn(promoCodes);
        when(mongoTemplate.count(any(Query.class), eq(PromoCode.class))).thenReturn((long) promoCodes.size());

        when(promoCodeMapper.handleEntity(any(PromoCode.class))).thenReturn(dto);

        PromoCodePackageDto result = readPromoCodeService.getFilteredPromoCodes(filter);

        assertThat(result).isNotNull();
        assertThat(result.totalElements()).isEqualTo(promoCodes.size());
        assertThat(result.pageNumber()).isEqualTo(filter.getPage());
        assertThat(result.pageSize()).isEqualTo(promoCodes.size());
    }

    @Test
    void getPromoCodeById_shouldReturnDto_whenExists() {

        when(validationManager.getPromoCodeByIdIfExists(VALID_ID)).thenReturn(promoCode);
        when(promoCodeMapper.handleEntity(promoCode)).thenReturn(dto);

        ResponsePromoCodeDto result = readPromoCodeService.getPromoCodeById(VALID_ID);

        assertThat(result).usingRecursiveComparison().isEqualTo(dto);
    }

    @Test
    void getCurrentPromoCodeByCode_shouldReturnEntity_whenExists() {

        when(validationManager.getCurrentPromoCode(promoCode.getPromoCode())).thenReturn(promoCode);

        PromoCode result = readPromoCodeService.getPromoCodeCurrentByCode(promoCode.getPromoCode());

        assertThat(result).isEqualTo(promoCode);
    }
}