Feature: Car Management

  Scenario: Add new car to driver
    Given I have a valid driver with ID 1
    And car number "ABC123" is unique
    When I add a new car for this driver
    Then car is saved and linked to driver

  Scenario: Delete existing car
    Given I have a car with ID 101
    When I delete it
    Then car is removed from database

  Scenario: Set current car for driver
    Given I have a car with ID 101
    When I set it as current
    Then all other cars become non-current
    And this car becomes current