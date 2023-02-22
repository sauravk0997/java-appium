package com.disney.qa.api.xray;

import com.disney.qa.api.disney.DisneyParameters;
import com.disney.qa.common.http.resttemplate.RestTemplateBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.URISyntaxException;

public class XrayUtility {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String OPTION_RUNNING = " AND \"Option\" = Running";
    private static final String JIRA_AUTH = "NzUxMDc2MDMyMDAzOmpZqnxxH2iWRbOUjZWgIoqmXy2Y";
    private static final String JQL = "jql";
    private static final String TOTAL = "total";
    private static final String XRAY_ENDPOINT = "/rest/api/2/search";

    protected final RestTemplate restTemplate = RestTemplateBuilder
            .newInstance()
            .withSpecificJsonMessageConverter()
            .withUtf8EncodingMessageConverter()
            .build();

    public String retrieveXrayTestCases(String jql, Boolean automated) throws IOException, URISyntaxException {
        URI uri = new URI(DisneyParameters.getJiraBaseUrl() + XRAY_ENDPOINT);
        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + JIRA_AUTH);
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject deviceObject = new JSONObject();
        if (automated) {
            deviceObject.put(JQL, jql + OPTION_RUNNING);
        } else {
            deviceObject.put(JQL, jql);
        }
        RequestEntity<String> request = new RequestEntity<>(deviceObject.toString(), headers, HttpMethod.POST, uri);

        LOGGER.debug("Jira JQL - Request: {}", request);

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        LOGGER.debug("Jira JQL - Response: {}", response);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response.getBody());
        String total = node.findValue(TOTAL).asText();
        if (automated) {
            LOGGER.info("Total number of test cases that are automated: {}", total);
        } else {
            LOGGER.info("Total number of test cases that are manual: {}", total);
        }
        return total;
    }
}
