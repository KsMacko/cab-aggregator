package com.internship.financeservice.unit.validation

import com.internship.financeservice.repo.DriverWalletRepo
import com.internship.financeservice.utils.UtilConstants.Companion.DEFAULT_ID
import com.internship.financeservice.utils.WalletUtil
import com.internship.financeservice.utils.exceptions.ExceptionCodes
import com.internship.financeservice.utils.exceptions.ResourceNotFoundException
import com.internship.financeservice.utils.validation.WalletValidationManager
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class WalletValidationManagerTest {

    @Mock
    private lateinit var walletRepo: DriverWalletRepo

    @InjectMocks
    private lateinit var validationManager: WalletValidationManager

    @Test
    fun getWalletIfExistsByDriverId_shouldReturnWallet_whenExists() {
        val wallet = WalletUtil.validDriverWallet()
        `when`(walletRepo.findByDriverId(DEFAULT_ID)).thenReturn(wallet)

        val result = validationManager.getWalletIfExistsByDriverId(DEFAULT_ID)

        assertThat(result).isEqualTo(wallet)
        verify(walletRepo).findByDriverId(DEFAULT_ID)
    }

    @Test
    fun getWalletIfExistsByDriverId_shouldThrow_whenNotFound() {
        `when`(walletRepo.findByDriverId(DEFAULT_ID)).thenReturn(null)

        assertThatThrownBy { validationManager.getWalletIfExistsByDriverId(DEFAULT_ID) }
            .isInstanceOf(ResourceNotFoundException::class.java)
            .hasMessageContaining(ExceptionCodes.WALLET_NOT_FOUND.getCode())
    }

    @Test
    fun getWalletIfExistsByDriverId_shouldThrow_whenRepoReturnsNull() {
        `when`(walletRepo.findByDriverId(DEFAULT_ID)).thenReturn(null)

        assertThatThrownBy { validationManager.getWalletIfExistsByDriverId(DEFAULT_ID) }
            .isInstanceOf(ResourceNotFoundException::class.java)
            .hasMessageContaining(ExceptionCodes.WALLET_NOT_FOUND.getCode())
    }
}