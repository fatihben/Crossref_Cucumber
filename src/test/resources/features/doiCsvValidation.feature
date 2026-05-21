Feature: Validate DOI list from CSV file

  Scenario: Validate all DOI numbers from CSV

    Given User reads DOI data from CSV file
    Then User sends requests for all DOI numbers
    And All responses should return status code 200
    And All responses should contain resolved status