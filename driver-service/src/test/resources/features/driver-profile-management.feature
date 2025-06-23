Feature: Driver Profile Management

  Scenario: Create new driver profile successfully
    Given I have a unique phone number "number"
    And I have a profile creation request with this phone number
    When I create the profile
    Then profile should be created and saved
    And wallet is created for driver

  Scenario: Update existing driver profile
    Given there is an existing driver profile with ID 1
    And current phone is "old-phone"
    And I have update request with phone "new-phone"
    When I update this profile with new phone number
    Then phone number is changed
    And profile is saved again

  Scenario: Delete driver profile
    Given there is an existing driver profile with ID 1
    When I delete this profile
    Then wallet is deleted
    And profile is removed from database