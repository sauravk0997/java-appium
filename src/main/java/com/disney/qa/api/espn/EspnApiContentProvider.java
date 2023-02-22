package com.disney.qa.api.espn;

import com.disney.qa.api.ApiContentProvider;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.core.MediaType;
import java.lang.invoke.MethodHandles;
import java.util.Map;

public class EspnApiContentProvider extends ApiContentProvider implements EspnContentProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String ESPN_SCHEDULE = "/calendar/accounts/";

    private static final String ESPN_SUBSCRIPTION = "/private/subscriptions";


    private String espnApiHost;

    protected RestTemplate restTemplate;

    private Map<String, String> headers = ImmutableMap.of("Content-Type", MediaType.APPLICATION_JSON);

    private String getEspnApiHost() {
        return espnApiHost;
    }

    protected void setEspnApiHost(String host) {
        this.espnApiHost = host;
    }

    @Override
    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String getHost() {
        return getEspnApiHost();
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    public <T> T postRetrieveSchedule(String binaryDataToSend, Class<T> returnType, String... pathSegments) {
        return postToEndPoint(ESPN_SCHEDULE, headers, binaryDataToSend, returnType, null, pathSegments);
    }

    @Override
    public <T> T getRetrieveEntitlements(Map<String, String> headers, Map<String, String> parameters, Class<T> returnType) {
        return getEndPoint(ESPN_SUBSCRIPTION, headers, parameters, returnType, null);
    }

    @Override
    public <T> T postCancelSubscription(Map<String, String> headers, String binaryDataToSend, Class<T> returnType, String... pathSegments) {
        return postToEndPoint(ESPN_SUBSCRIPTION,headers,binaryDataToSend,returnType,null,pathSegments);
    }

    @Override
    public <T> T postAddSubscription(Map<String, String> headers, String binaryDataToSend, Class<T> returnType) {
        return postToEndPoint(ESPN_SUBSCRIPTION,headers,binaryDataToSend,returnType);
    }
}
