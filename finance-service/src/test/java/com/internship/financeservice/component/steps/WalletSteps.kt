package com.internship.financeservice.component.steps

import com.internship.financeservice.dto.mapper.WalletMapper
import com.internship.financeservice.entity.DriverWallet
import com.internship.financeservice.repo.DriverWalletRepo
import com.internship.financeservice.service.wallet.WalletService
import com.internship.financeservice.utils.validation.WalletValidationManager
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class WalletSteps {
    @Mock
    private lateinit var walletRepo: DriverWalletRepo

    @Mock
    private lateinit var walletValidatorManager: WalletValidationManager

    @Mock
    private lateinit var walletMapper: WalletMapper

    private var currentDriverId: Long? = null

    @InjectMocks
    private lateinit var walletService: WalletService

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        `when`(walletRepo.save(any())).thenReturn(mock<DriverWallet>())
        doNothing().`when`(walletRepo).deleteByDriverId(anyLong())
    }

    @Given("I have a driver with ID {long}")
    fun i_have_a_driver_with_id(driverId: Long?) {
        currentDriverId = driverId
    }

    @When("I create wallet for them")
    fun i_create_wallet_for_them() {
        walletService.createWallet(currentDriverId!!)
    }

    @When("I delete wallet")
    fun i_delete_wallet() {
        walletService.deleteWallet(currentDriverId!!)
    }

    @Then("wallet is saved in database")
    fun wallet_is_saved_in_database() {
        verify(walletRepo).save(any())
    }

    @Then("wallet is removed from database")
    fun wallet_is_removed_from_database() {
        verify(walletRepo).deleteByDriverId(currentDriverId!!)
    }
}