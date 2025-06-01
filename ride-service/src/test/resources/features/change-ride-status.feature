Feature: Ride Service Business Logic

  Scenario: Status ACCEPTED with driver
    Given I have a ride with ID "ride-111"
    When I change status to ACCEPTED with driver ID 1
    Then ride should have driver ID 1
    And validation manager should be called once

  Scenario: Status WAIT_FOR_PASSENGER
    Given I have a ride with ID "ride-111"
    When I change status to WAIT_FOR_PASSENGER
    Then ride should be updated with start waiting time
    And validation manager should be called once

  Scenario: Status RECALCULATED
    Given I have a ride with ID "ride-111"
    When I change status to RECALCULATED
    Then price is recalculated and saved

  Scenario: Status COMPLETED and payment by CARD
    Given I have a ride with ID "ride-111" and payment type "CARD"
    When I change status to COMPLETED
    Then financeFeignClient.createCardPayment should be called once

  Scenario: Status COMPLETED and payment by CASH
    Given I have a ride with ID "ride-111" and payment type "CASH"
    When I change status to COMPLETED
    Then kafkaProducer.sendPaymentByCashConfirmation should be called once