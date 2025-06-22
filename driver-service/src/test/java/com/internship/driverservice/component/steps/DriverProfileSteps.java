package com.internship.driverservice.component.steps;

import com.internship.driverservice.dto.mapper.ProfileMapper;
import com.internship.driverservice.dto.request.RequestProfileDto;
import com.internship.driverservice.entity.DriverProfile;
import com.internship.driverservice.repo.DriverProfileRepo;
import com.internship.driverservice.service.command.CommandDriverProfileService;
import com.internship.driverservice.service.communication.FinanceFeignClient;
import com.internship.driverservice.utils.validation.ProfileValidationManager;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DriverProfileSteps {

    @Mock
    private ProfileValidationManager profileValidationManager;

    @Mock
    private FinanceFeignClient financeFeignClient;

    @Mock
    private DriverProfileRepo driverProfileRepo;

    @Mock
    private ProfileMapper profileMapper;

    @InjectMocks
    private CommandDriverProfileService commandDriverProfileService;

    private String currentPhone;
    private RequestProfileDto profileDto;
    private DriverProfile existingProfile;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Given("I have a unique phone number {string}")
    public void i_have_a_unique_phone(String phone) {
        this.currentPhone = phone;
        doNothing().when(profileValidationManager).checkIfPhoneUnique(phone);
    }

    @Given("I have a profile creation request with this phone number")
    public void i_have_profile_creation_request_with_phone() {
        this.profileDto = RequestProfileDto.builder()
                .phone(this.currentPhone)
                .build();
    }

    @Given("there is an existing driver profile with ID {long}")
    public void there_is_existing_driver_profile(Long id) {
        this.existingProfile = mock(DriverProfile.class);
        when(existingProfile.getProfileId()).thenReturn(id);
        when(existingProfile.getPhone()).thenReturn("old-phone");

        when(profileValidationManager.getDriverProfile(id)).thenReturn(existingProfile);
    }

    @Given("current phone is {string}")
    public void current_phone_is(String phone) {
        when(existingProfile.getPhone()).thenReturn(phone);
    }

    @Given("I have update request with phone {string}")
    public void i_have_update_request_with_phone(String phone) {
        this.profileDto = RequestProfileDto.builder()
                .phone(phone)
                .build();

        when(profileMapper.handleDto(profileDto)).thenReturn(
                DriverProfile.builder()
                        .phone(phone)
                        .build()
        );
    }

    @When("I create the profile")
    public void i_create_the_profile() {
        DriverProfile driverProfile = mock(DriverProfile.class);

        when(driverProfile.getPhone()).thenReturn(this.currentPhone);
        when(profileMapper.handleDto(any(RequestProfileDto.class))).thenReturn(driverProfile);
        when(driverProfileRepo.save(driverProfile)).thenAnswer(invocation -> {
            DriverProfile profile = invocation.getArgument(0);
            when(profile.getProfileId()).thenReturn(1L);
            return profile;
        });

        commandDriverProfileService.createProfile(profileDto);
    }

    @When("I update this profile with new phone number")
    public void i_update_profile_with_id() {
        doAnswer(invocation -> {
            RequestProfileDto dto = invocation.getArgument(0);
            DriverProfile profile = invocation.getArgument(1);
            profile.setPhone(dto.phone());
            return null;
        })
                .when(profileMapper)
                .updateProfileFromDto(any(RequestProfileDto.class), any(DriverProfile.class));

        commandDriverProfileService.updateDriverProfile(existingProfile.getProfileId(), profileDto);
    }

    @When("I delete this profile")
    public void i_delete_profile_with_id() {
        commandDriverProfileService.deleteDriverProfile(existingProfile.getProfileId());
    }

    @Then("profile should be created and saved")
    public void profile_should_be_created_and_saved() {
        verify(driverProfileRepo).save(any(DriverProfile.class));
    }

    @Then("wallet is created for driver")
    public void wallet_is_created_for_driver() {
        verify(financeFeignClient).createWallet(anyLong());
    }

    @Then("wallet is deleted")
    public void wallet_is_deleted() {
        verify(financeFeignClient).deleteWallet(anyLong());
    }

    @Then("profile is removed from database")
    public void profile_is_removed_from_database() {
        verify(driverProfileRepo).deleteById(anyLong());
    }

    @Then("phone number is changed")
    public void phone_is_changed_to() {
        verify(existingProfile).setPhone(anyString());
    }

    @Then("profile is saved again")
    public void profile_is_saved_again() {
        verify(driverProfileRepo, atLeastOnce()).save(existingProfile);
    }
}