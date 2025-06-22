Feature: Finance Operation Management

  Scenario: Create payment by cash
    Given I am a passenger with ID 1 brought to the place by driver with ID 1
    And The ride's cost is 15.00
    And I have a confirmed cash payment request
    When I process it
    Then financial operation is created
    And payment is saved to DB

  Scenario: Create payment by card
    Given I am a passenger with ID 1 brought to the place by driver with ID 1
    And The ride's cost is 15.00
    And I have a confirmed card payment request
    When I process it
    Then financial operation is created
    And payment is saved to DB
    And driver wallet is updated

  Scenario: Transfer money between wallets
    Given I have a wallet transfer request for driver 1 on amount 120.20
    When I process it
    Then wallet balance is updated
    And transfer is saved in database