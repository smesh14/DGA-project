package com.contactbook.cucumberglue;

import com.contactbook.repository.ContactRepository;
import com.contactbook.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
public class CucumberSteps {
    @LocalServerPort
    Integer port;
    ResponseEntity<String> lastResponse;

    String jwtToken = "";

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;


    @After
    public void cleanTables(){
        contactRepository.deleteAll();
        userRepository.deleteAll();
    }



    @When("the client calls endpoint {string} with body")
    public void whenClientCalls(String url,String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        if (jwtToken != null && !url.equals("/api/user/login")) {
            headers.add("Authorization", "Bearer " + jwtToken);
        }
        try {
            if(url.contains("update")){
                lastResponse = new RestTemplate().exchange("http://localhost:" + port + url, HttpMethod.PUT, entity,
                        String.class);
            }else {
                lastResponse = new RestTemplate().exchange("http://localhost:" + port + url, HttpMethod.POST, entity,
                        String.class);
            }
            if (url.equals("/api/user/login")) {
                Map<String, Object> responseMap = objectMapper.readValue(lastResponse.getBody(), new TypeReference<Map<String, Object>>() {});
                jwtToken = (String) responseMap.get("accessToken");
            }
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @When("the client calls endpoint {string}")
    public void theClientCallsEndpoint(String url) {
        try {
            lastResponse = new RestTemplate().exchange("http://localhost:" + port + url, HttpMethod.GET, null,
                    String.class);
        } catch (HttpClientErrorException httpClientErrorException) {
            httpClientErrorException.printStackTrace();
        }
    }


    @Then("response status code is {int}")
    public void thenStatusCodee(int expected) {
        Assertions.assertNotNull(lastResponse);
        Assertions.assertNotNull(lastResponse.getStatusCode());
        assertThat("status code is" + expected,
                lastResponse.getStatusCode().value() == expected);
    }


    @Then("response status code is not present")
    public void thenStatusCodeeIsNotPresent() {
        Assertions.assertNull(lastResponse);
    }

    @Then("returned string contains {string}")
    public void thenStringIs(String expected) {
        Assertions.assertTrue(Objects.requireNonNull(lastResponse.getBody()).contains(expected));
    }



}
