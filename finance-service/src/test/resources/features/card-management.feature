Feature: Card Management

  Scenario: Create new card successfully
    Given I have a valid card with last 4 digits "1111" and type "VISA"
    And Card's owner is "DRIVER" and his ID is 1
    And Expiration date is "2029-11-01"
    When I send card creation request
    Then card is saved to database

  Scenario: Delete existing card
    Given I have a card with ID 101
    When I delete card
    Then card is removed from database