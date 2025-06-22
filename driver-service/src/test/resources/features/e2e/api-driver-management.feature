Feature: Driver Profile Management

  Scenario: Create a new driver profile and retrieve it
    Given I have driver info
    When I send a POST request to create a driver profile
    Then The response has status code 201
    And the created profile should match the sent data

  Scenario: Delete a driver profile
    Given A driver profile exists
    When I delete the driver profile
    Then The response has status code 204
    And GET request for that driver returns 404