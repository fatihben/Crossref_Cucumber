Feature: XML file validation

  Scenario: Validate XML file as positive test
    Given I send a valid GET request to CrossRef
    Then Validate Status Code
    Then Verify title
    Then Assert DOI number
