Feature: XML file negative validation

  Scenario: Validate XML file as negative test
    Given I send an unauthorized GET request to CrossRef
    Then Assert DOI number not correct
    Then Conclude the test