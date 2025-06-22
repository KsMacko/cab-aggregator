Feature: Car management for drivers

  Scenario: Create a car and set it as current
    Given I have driver information
    When I send a POST request to create profile
    Then the response should return status code 201
    And I have car information
    When I send a POST request to create car
    Then the response should return status code 200
    When I send a PATCH request to set created car as current
    Then the car is marked as current

  Scenario: Delete a car and check it's removed
    Given I send a POST request to create profile
    And I send a POST request to create car
    When I delete the car
    Then the response should return status code 204