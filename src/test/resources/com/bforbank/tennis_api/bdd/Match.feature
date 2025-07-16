Feature: Match API

  Scenario: Retrieve a match by ID
    Given a match exists with id "1"
    When I GET "/api/v1/matches/1"
    Then the response status should be 200
    And the response should contain the match details

  Scenario: List all matches
    Given at least one match exists
    When I GET "/api/v1/matches"
    Then the response status should be 200
    And the response should contain a list of matches

  Scenario: Create a match from score
    When I POST "/api/v1/matches/score" with valid score data
    Then the response status should be 200
    And the response should contain the created match