Feature: Ria Money Transfer Calculator Tests
  As a user, I want to verify the Ria calculator
  to ensure that validations and conversions work correctly.

  Background:
    Given A user goes to Ria Money Transfer website

  @Calculator
  Scenario: TC_Validate_Calculator_Behaviour
    Given The user types "25000" into the amount field
    # the amount field already avoids alphabetic characters
    When The user selects the country "Haiti" in the search field with "HTG" currency
    Then the conversion should be performed successfully