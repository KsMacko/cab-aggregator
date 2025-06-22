package com.internship.rideservice.unit.validation;

import com.internship.rideservice.entity.PromoCode;
import com.internship.rideservice.repo.PromoCodeRepo;
import com.internship.rideservice.util.exceptions.InvalidInputException;
import com.internship.rideservice.util.exceptions.ResourceNotFoundException;
import com.internship.rideservice.util.validators.PromoCodeValidationManager;
import com.internship.rideservice.utils.PromoCodeUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.internship.rideservice.util.exceptions.ExceptionCodes.NO_CURRENT_PROMO_CODE;
import static com.internship.rideservice.util.exceptions.ExceptionCodes.PROMO_CODE_NOT_FOUND;
import static com.internship.rideservice.util.exceptions.ExceptionCodes.PROMO_CODE_STILL_VALID;
import static com.internship.rideservice.utils.UtilConstants.VALID_ID;
import static com.internship.rideservice.utils.UtilConstants.VALID_PROMO_CODE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PromoCodeValidationManagerTest {

    @Mock
    private PromoCodeRepo promoCodeRepo;

    @InjectMocks
    private PromoCodeValidationManager validationManager;

    private static final PromoCode promocode = PromoCodeUtil.promoCodeEntity();

    @Test
    void checkIfExistsById_shouldThrow_whenNotFound() {
        when(promoCodeRepo.existsById(anyString())).thenReturn(false);

        assertThatThrownBy(() -> validationManager.checkIfExistsById(VALID_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(PROMO_CODE_NOT_FOUND.getCode());
    }

    @Test
    void checkIfExistsById_shouldDoNothing_whenExists() {
        when(promoCodeRepo.existsById(anyString())).thenReturn(true);

        validationManager.checkIfExistsById(VALID_ID);
    }

    @Test
    void getPromoCodeByIdIfExists_shouldReturn_whenExists() {

        when(promoCodeRepo.findById(anyString())).thenReturn(Optional.of(promocode));

        PromoCode result = validationManager.getPromoCodeByIdIfExists(VALID_ID);

        assertThat(result).isEqualTo(promocode);
    }

    @Test
    void getPromoCodeByIdIfExists_shouldThrow_whenNotExists() {
        when(promoCodeRepo.findById(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> validationManager.getPromoCodeByIdIfExists(VALID_ID))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(PROMO_CODE_NOT_FOUND.getCode());
    }

    @Test
    void checkForCreationPromoCodeValidity_shouldThrow_whenPromoCodeStillValid() {

        when(promoCodeRepo.findByPromoCodeAndValidUntilAfter(anyString(), any(LocalDateTime.class)))
                .thenReturn(Optional.of(promocode));

        assertThatThrownBy(() -> validationManager.checkForCreationPromoCodeValidity(VALID_PROMO_CODE))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining(PROMO_CODE_STILL_VALID.getCode());
    }

    @Test
    void checkForCreationPromoCodeValidity_shouldDoNothing_whenNoActivePromoCode() {

        when(promoCodeRepo.findByPromoCodeAndValidUntilAfter(anyString(), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        validationManager.checkForCreationPromoCodeValidity(VALID_PROMO_CODE);
    }

    @Test
    void getCurrentPromoCode_shouldReturn_whenValid() {

        when(promoCodeRepo.findByPromoCodeAndValidUntilAfter(anyString(), any(LocalDateTime.class)))
                .thenReturn(Optional.of(promocode));

        PromoCode result = validationManager.getCurrentPromoCode(VALID_PROMO_CODE);

        assertThat(result).isEqualTo(promocode);
    }

    @Test
    void getCurrentPromoCode_shouldThrow_whenNoCurrentPromoCode() {

        when(promoCodeRepo.findByPromoCodeAndValidUntilAfter(anyString(), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> validationManager.getCurrentPromoCode(VALID_PROMO_CODE))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(NO_CURRENT_PROMO_CODE.getCode());
    }
}