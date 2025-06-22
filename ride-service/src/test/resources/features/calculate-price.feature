Feature: Calculate ride price based on multiple factors

  Scenario: Base price calculated by distance only
    Given I have a ride with ID "ride-123" with duration 5 minutes and distance 10 km
    And waiting time was 2 minutes
    And fare type is "ECONOMY" with price per km 0.4 and per minute 0.5
    And fare min price is 5 with 5 minutes for free waiting and payment for waiting is 0.5 per minute
    And promo code is not applied
    When I recalculate price
    Then total price equals min price plus distance multiply price per km

  Scenario: Price based on time because it's higher
    Given I have a ride with ID "ride-456" with duration 20 minutes and distance 10 km
    And waiting time was 3 minutes
    And fare type is "ECONOMY" with price per km 0.4 and per minute 0.5
    And fare min price is 3 with 3 minutes for free waiting and payment for waiting is 0.7 per minute
    When I recalculate price
    Then total price should be based on time

  Scenario: Promo code applies discount
    Given I have a ride with ID "ride-789" with duration 20 minutes and distance 15 km
    And waiting time was 3 minutes
    And fare type is "ECONOMY" with price per km 0.5 and per minute 0.5
    And fare min price is 5 with 5 minutes for free waiting and payment for waiting is 1.0 per minute
    And promo code "SUMMER25" gives 25% discount
    When I recalculate price
    Then total price should be reduced by 25%

  Scenario: Extra waiting time charged
    Given I have a ride with ID "ride-101" with duration 5 minutes and distance 3 km
    And waiting time was 10 minutes
    And fare type is "COMFORT" with price per km 2.5 and per minute 1.4
    And fare min price is 7 with 5 minutes for free waiting and payment for waiting is 2.0 per minute
    When I recalculate price
    Then total price includes 5 minutes of paid waiting time

  Scenario: All conditions together
    Given I have a ride with ID "ride-111" with duration 25 minutes and distance 10 km
    And waiting time was 10 minutes
    And fare type is "COMFORT" with price per km 2.5 and per minute 1.4
    And fare min price is 7 with 3 minutes for free waiting and payment for waiting is 2.0 per minute
    And promo code "VIP" gives 10% discount
    When I recalculate price
    Then final price is time-based plus 7 minutes of paid waiting with 10% discount