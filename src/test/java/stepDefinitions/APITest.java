package stepDefinitions;

import io.cucumber.java.en.*;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import utilities.CsvReader;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;


public class APITest {
  public static Response response;
  public static XmlPath xml;
    List<String> doiList;

    List<Response> responses = new ArrayList<>();

    @Given("I send a valid GET request to CrossRef")
    public void i_send_a_valid_get_request_to_cross_ref() {
        response =
                given()
                        .header("User-Agent", "Mozilla/5.0")
                        .header("Accept", "application/xml")

                        .queryParam("pid", "sahinfatih@gmail.com")
                        .queryParam("id", "doi:10.1007/s10696-011-9101-8")
                        .queryParam("noredirect", "true")
                        .when()

                        .get("https://www.crossref.org/openurl");
    }
    @Then("Validate Status Code")
    public void validate_status_code() {
        response.then().statusCode(200);
       }
    @Then("Verify title")
    public void verify_title() {
     xml = new XmlPath(response.asString());

        String journalTitle =
                xml.getString("**.find { it.name() == 'article_title' }");

        assertNotNull(journalTitle);
        System.out.println(journalTitle);
      assertTrue(journalTitle.equalsIgnoreCase("Joint order batching and picker routing in single and multiple-cross-aisle warehouses using cluster-based tabu search algorithms"));

    }
    @Then("Assert DOI number")
    public void assert_doi_number() {
      String doi=xml.getString("**.find { it.name() == 'doi' }");

        assertNotNull(doi);
        assertEquals("10.1007/s10696-011-9101-8", doi);
    }

    @Given("I send an unauthorized GET request to CrossRef")
    public void i_send_an_unauthorized_get_request_to_cross_ref() {
        response =
                given()
                        .header("User-Agent", "Mozilla/5.0")
                        .header("Accept", "application/xml")

                        .queryParam("pid", "55555")
                        .queryParam("id", "doi:10.1007/s10006-010-9101-8")
                        .queryParam("noredirect", "true")
                        .when()

                        .get("https://www.crossref.org/openurl");
        String body = response.asString();
        assertNotNull(response.asString(), "No response from server!");

        assertTrue(body.contains("The login you supplied is not recognized"));

    }


    @Then("Assert DOI number not correct")
    public void assert_doi_number_not_correct() {
        response =
                given()
                        .header("User-Agent", "Mozilla/5.0")
                        .header("Accept", "application/xml")

                        .queryParam("pid", "sahinfatih@gmail.com")
                        .queryParam("id", "doi:10.1007/s10000-011-9101-8")
                        .queryParam("noredirect", "true")
                        .when()

                        .get("https://www.crossref.org/openurl");

        xml = new XmlPath(response.asString());

        String noJournal =
                xml.getString("**.find { it.name() == 'doi_batch_id' }");

        assertTrue(noJournal.equalsIgnoreCase("none"));


    }

    @Given("User reads DOI data from CSV file")
    public void user_reads_doi_data_from_csv_file() {

        doiList = CsvReader.getDoisFromCsv(
                "src/test/resources/testdata/doiList.csv"
        );

        assertFalse(doiList.isEmpty());
    }
    @Then("User sends requests for all DOI numbers")
    public void user_sends_requests_for_all_doi_numbers() {
        for (String doi : doiList) {
            Response response = given()
                            .header("User-Agent","Mozilla/5.0")
                            .header("Accept","application/xml")
                            .queryParam("pid","sahinfatih@gmail.com")
                            .queryParam("id","doi:" + doi)
                            .queryParam("noredirect","true")
                            .when()
                            .get("https://www.crossref.org/openurl");
            responses.add(response);
        }
    }
    @Then("All responses should return status code {int}")
    public void all_responses_should_return_status_code(Integer int1) {
        for (Response response : responses) {
            assertEquals(200, response.getStatusCode());
        }
    }
    @Then("All responses should contain resolved status")
    public void all_responses_should_contain_resolved_status() {
        for (Response response : responses) {

            String body = response.asString();

            System.out.println("FULL RESPONSE:");
            System.out.println(body);

            XmlPath xml = new XmlPath(body);

            String status = xml.getString(
                    "**.find { it.name() == 'query' }.@status"
            );

            System.out.println("EXTRACTED STATUS -> [" + status + "]");

            assertEquals(
                    "STATUS FAILED! BODY: \n" + body,
                    "resolved",
                    status
            );
        }

    }}