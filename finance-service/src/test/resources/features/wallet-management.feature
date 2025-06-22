Feature: Wallet Management

  Scenario: Create wallet for driver
    Given I have a driver with ID 1
    When I create wallet for them
    Then wallet is saved in database

  Scenario: Delete wallet for driver
    Given I have a driver with ID 1
    When I delete wallet
    Then wallet is removed from database