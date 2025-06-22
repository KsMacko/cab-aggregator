Feature: Promo Code Management

  Scenario: Create a new promo code and retrieve it
    Given I have a valid promo code request
    When I send a POST request to create a promo code "ProMo23"
    Then The response has status code 201
    And the created promo code should match the sent data
    And I can GET the promo code by code and receive the same data

  Scenario: Delete a promo code
    Given I have a valid promo code request
    When I send a POST request to create a promo code "SUM25"
    And I delete the promo code
    Then The response has status code 204
    And GET request for that promo code returns 404