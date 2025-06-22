Feature: Rate Management for Driver and Passenger

  Scenario: Driver rates a passenger
    Given I have a valid rate request for passenger
    When I send a POST request to create a passenger profile with number "375447777777" and email "ex@ex.com"
    And I send a POST request to rate passenger
    Then The response has status code 200

  Scenario: Delete driver's rate from passenger
    Given a rate exists for passenger
    When I send a DELETE request to delete rate from passenger
    Then The response has status code 204

  Scenario: Passenger rates a driver
    Given I have a valid rate request for driver
    When I send a POST request to create a passenger profile with number "375336666666" and email "pp@ppp.com"
    And I send a POST request to rate driver
    Then The response has status code 200

  Scenario: Delete passenger's rate from driver
    Given a rate exists for driver
    When I send a DELETE request to delete rate from driver
    Then The response has status code 204