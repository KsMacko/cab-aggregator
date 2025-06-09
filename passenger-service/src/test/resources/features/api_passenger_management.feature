Feature: Passenger Profile Management

  Scenario: Create a new passenger profile and retrieve it
    Given I send a POST request to create a passenger profile with number "375291234567" and email "pp@ppp.com"
    Then the response should return status code 201
    And the created profile should match the sent data
    And I can GET the passenger by ID and receive the same data

  Scenario: Delete a passenger profile
    Given I have a valid passenger profile
    When I send a POST request to create a passenger profile with number "375291234567" and email "uu@unique.com"
    And I delete the passenger profile
    Then the response should return status code 204
    And GET request for that passenger returns 404