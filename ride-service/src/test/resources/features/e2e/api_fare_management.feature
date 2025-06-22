Feature: Fare Management

  Scenario: Create a new fare and retrieve it
    Given I have a valid fare request
    When I send a POST request to create a fare
    Then the response should return status code 201
    And the created fare should match the sent data
    And I can GET the fare by type and receive the same data

  Scenario: Delete a fare
    Given I have a valid fare request
    When I send a POST request to create a fare
    And I delete the fare
    Then the response should return status code 204
    And GET request for that fare returns 404