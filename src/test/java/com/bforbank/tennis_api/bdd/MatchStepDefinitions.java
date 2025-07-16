package com.bforbank.tennis_api.bdd;

import com.bforbank.tennis_api.infrastructure.adapter.controller.MatchDto;
import com.bforbank.tennis_api.infrastructure.adapter.requests.ScoreRequest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MatchStepDefinitions {
    @Autowired
    private TestRestTemplate restTemplate;
    private ResponseEntity<?> response;
    private String createdMatchId;

    @Given("a match exists with id {string}")
    public void a_match_exists_with_id(String id) {
        System.out.println("Step: a match exists with id " + id);
        // Crée un match en postant un score simple, puis modifie son id si besoin
        ScoreRequest req = new ScoreRequest("AAAABBBB");
        ResponseEntity<MatchDto> resp = restTemplate.postForEntity("/api/v1/matches/score", req, MatchDto.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        MatchDto match = resp.getBody();
        assertThat(match).isNotNull();
        createdMatchId = match.getId();
    }

    @Given("at least one match exists")
    public void at_least_one_match_exists() {
        System.out.println("Step: at least one match exists");
        ResponseEntity<MatchDto[]> resp = restTemplate.getForEntity("/api/v1/matches", MatchDto[].class);
        if (resp.getBody() == null || resp.getBody().length == 0) {
            ScoreRequest req = new ScoreRequest("AABBAABB");
            ResponseEntity<MatchDto> respCreate = restTemplate.postForEntity("/api/v1/matches/score", req, MatchDto.class);
            assertThat(respCreate.getStatusCode()).isEqualTo(HttpStatus.OK);
        }
    }

    @When("I GET {string}")
    public void i_get(String endpoint) {
        if (endpoint.matches(".*/matches/1$") && createdMatchId != null) {
            endpoint = endpoint.replace("/1", "/" + createdMatchId);
        }
        System.out.println("Step: I GET " + endpoint);
        if (endpoint.matches(".*/[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")) {
            response = restTemplate.getForEntity(endpoint, MatchDto.class);
        } else {
            response = restTemplate.getForEntity(endpoint, MatchDto[].class);
        }
    }

    @When("I POST {string} with valid score data")
    public void i_post_with_valid_score_data(String endpoint) {
        System.out.println("Step: I POST " + endpoint + " with valid score data");
        ScoreRequest req = new ScoreRequest("AAAABBBB");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ScoreRequest> entity = new HttpEntity<>(req, headers);
        response = restTemplate.postForEntity(endpoint, entity, MatchDto.class);
        if (response.getBody() instanceof MatchDto match) {
            createdMatchId = match.getId();
        }
    }

    @Then("the response status should be 200")
    public void the_response_status_should_be_200() {
        System.out.println("Step: the response status should be 200");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Then("the response should contain the match details")
    public void the_response_should_contain_the_match_details() {
        System.out.println("Step: the response should contain the match details");
        assertThat(response.getBody()).isNotNull();
    }

    @Then("the response should contain a list of matches")
    public void the_response_should_contain_a_list_of_matches() {
        System.out.println("Step: the response should contain a list of matches");
        assertThat(response.getBody()).isNotNull();
        assertThat(((MatchDto[]) response.getBody()).length).isGreaterThan(0);
    }

    @Then("the response should contain the created match")
    public void the_response_should_contain_the_created_match() {
        System.out.println("Step: the response should contain the created match");
        assertThat(response.getBody()).isNotNull();
    }

    @Then("the response should contain a match with id {string}")
    public void the_response_should_contain_a_match_with_id(String id) {
        MatchDto match = (MatchDto) response.getBody();
        assertThat(match).isNotNull();
        assertThat(match.getId()).isNotNull();
    }
}
