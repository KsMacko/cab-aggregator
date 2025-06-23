Feature: Rating Service

  Scenario: Set rate to driver successfully
    Given I have a ride with ID "ride-123"
    And I have a rate from passenger with ID 1 to driver with ID 1
    When I get the rate from passenger
    Then rate should be saved for driver
    And ride service confirms participants
    And rate is saved to database

  Scenario: Set rate to passenger successfully
    Given I have a ride with ID "ride-123"
    And I have a rate to passenger with ID 1 from driver with ID 1
    When I send the rate to passenger
    Then rate should be send to passenger service

  Scenario: Delete driver's rate
    Given I am a passenger with ID 1
    And I have rate with ID 1
    When I delete this rate from driver
    Then author is checked by request to passenger service
    And rate is removed from DB

  Scenario: Delete rate from passenger
    Given I am a driver with ID 1
    And I have rate with ID 1
    When I delete this rate from passenger
    Then the request is sent to passenger service