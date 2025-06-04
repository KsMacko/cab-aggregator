package com.internship.financeservice.unit.service

import com.internship.financeservice.dto.mapper.WalletMapper
import com.internship.financeservice.repo.DriverWalletRepo
import com.internship.financeservice.service.wallet.WalletService
import com.internship.financeservice.utils.UtilConstants.Companion.DEFAULT_ID
import com.internship.financeservice.utils.WalletUtil
import com.internship.financeservice.utils.exceptions.ExceptionCodes
import com.internship.financeservice.utils.exceptions.ResourceNotFoundException
import com.internship.financeservice.utils.validation.WalletValidationManager
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

@ExtendWith(MockitoExtension::class)
class WalletServiceTest{

    @Mock
    private lateinit var walletRepo: DriverWalletRepo

    @Mock
    private lateinit var walletValidatorManager: WalletValidationManager

    @Mock
    private lateinit var walletMapper: WalletMapper

    @InjectMocks
    private lateinit var walletService: WalletService

    private val validWallet = WalletUtil.validDriverWallet()

    @Test
    fun createWallet_shouldCreateNewWallet_whenValidId() {
        `when`(walletRepo.save(any())).thenReturn(validWallet)

        val result = walletService.createWallet(DEFAULT_ID)

        verify(walletRepo).save(any())
        assertThat(result.driverId).isEqualTo(DEFAULT_ID)
    }

    @Test
    fun deleteWallet_shouldCallValidatorAndDelete_whenExists() {
        val id = DEFAULT_ID

        `when`(walletValidatorManager.getWalletIfExistsByDriverId(id)).thenReturn(validWallet)

        walletService.deleteWallet(id)

        verify(walletValidatorManager).getWalletIfExistsByDriverId(id)
        verify(walletRepo).deleteByDriverId(id)
    }

    @Test
    fun deleteWallet_shouldThrow_whenNotFound() {
        doThrow(ResourceNotFoundException(ExceptionCodes.WALLET_NOT_FOUND.getCode()))
            .`when`(walletValidatorManager).getWalletIfExistsByDriverId(DEFAULT_ID)

        assertThatThrownBy { walletService.deleteWallet(DEFAULT_ID) }
            .isInstanceOf(ResourceNotFoundException::class.java)
            .hasMessageContaining(ExceptionCodes.WALLET_NOT_FOUND.getCode())
    }

    @Test
    fun findAllWallets_shouldReturnMappedDto_whenValidFilter() {
        val filter = WalletUtil.walletFilterRequest()
        val page = PageImpl(listOf(validWallet))

        `when`(walletRepo.findAll(any(), any<Pageable>())).thenReturn(page)
        `when`(walletMapper.toDto(validWallet)).thenReturn(WalletUtil.walletDto())

        val result = walletService.findAllWallets(filter)

        assertThat(result.wallets).hasSize(1)
        assertThat(result.totalElements).isEqualTo(1)
        assertThat(result.page).isEqualTo(filter.page)
        assertThat(result.size).isEqualTo(page.size)
        assertThat(result.totalPages).isEqualTo(page.totalPages)
    }

    @Test
    fun getWalletById_shouldReturnWallet_whenFound() {
        `when`(walletValidatorManager.getWalletIfExistsByDriverId(DEFAULT_ID))
            .thenReturn(validWallet)

        val result = walletService.getWalletById(DEFAULT_ID)

        assertThat(result).isEqualTo(validWallet)
    }

    @Test
    fun getWalletById_shouldThrow_whenNotFound() {
        `when`(walletValidatorManager.getWalletIfExistsByDriverId(DEFAULT_ID))
            .thenThrow(ResourceNotFoundException(ExceptionCodes.WALLET_NOT_FOUND.getCode()))

        assertThatThrownBy { walletService.getWalletById(DEFAULT_ID) }
            .isInstanceOf(ResourceNotFoundException::class.java)
            .hasMessageContaining(ExceptionCodes.WALLET_NOT_FOUND.getCode())
    }
}