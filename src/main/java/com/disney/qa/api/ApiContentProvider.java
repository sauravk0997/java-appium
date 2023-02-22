package com.disney.qa.api;

import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

/**
 * Abstract class which provides methods for dealing with REST API
 */
public abstract class ApiContentProvider {

    protected boolean isNoCacheHeader = false;

    public abstract String getHost();

    public abstract RestTemplate getRestTemplate();

    protected abstract Logger getLogger();

    /**
     * Method for performing GET request for REST endpoint
     */
    protected <T> T getEndPoint(String methodPath, Map<String, String> headers, Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return gEndPoint(methodPath, headers, parameters, returnType, null, pathSegments);
    }

    private <T> T gEndPoint(String methodPath, Map<String, String> headers, Map<String, String> parameters, Class<T> returnType, String returnedInfo, String... pathSegments) {
        MultiValueMap requestParameters = new LinkedMultiValueMap();
        MultiValueMap requestHeaders = new LinkedMultiValueMap();

        if (parameters != null) {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                requestParameters.add(entry.getKey(), entry.getValue());
            }
        }

        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestHeaders.add(entry.getKey(), entry.getValue());
            }
        }

        if (isNoCacheHeader) {
            requestHeaders.add(HttpHeaders.CACHE_CONTROL, "no-cache");
        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(getHost()).path(methodPath).queryParams(requestParameters);

        if (pathSegments != null) {
            builder = builder.pathSegment(pathSegments);
        }

        URI uri = builder.build().toUri();

        RequestEntity<?> readRequestEntity = new RequestEntity(requestHeaders, HttpMethod.GET, uri);

        getLogger().info(String.format("Requesting API endpoint '%s': %s", methodPath, uri));

        return executeCalls(readRequestEntity, returnType, returnedInfo);
    }

    protected <T> T postToEndPoint(String urlPath, Map<String, String> headers, String binaryDataToSend, Class<T> returnType) {
        return pToEndPoint(urlPath, headers, binaryDataToSend, returnType, HttpMethod.POST);
    }

    protected <T> T postToEndPoint(String urlPath, Map<String, String> headers, String binaryDataToSend, Class<T> returnType, String returnedInfo, String... pathSegments) {
        return pToEndPoint(urlPath, headers, binaryDataToSend, returnType, HttpMethod.POST, returnedInfo, pathSegments);
    }

    private <T> T pToEndPoint(String urlPath, Map<String, String> headers, String binaryDataToSend, Class<T> returnType, HttpMethod httpMethod) {
        return pToEndPoint(urlPath, headers, binaryDataToSend, returnType, httpMethod, null);
    }

    private <T> T pToEndPoint(String urlPath, Map<String, String> headers, String binaryDataToSend, Class<T> returnType, HttpMethod httpMethod, String returnedInfo, String... pathSegments) {

        MultiValueMap requestHeaders = new LinkedMultiValueMap();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getHost()).path(urlPath);

        if (pathSegments != null) {
            builder = builder.pathSegment(pathSegments);
        }

        URI uri = builder.build().toUri();

        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestHeaders.add(entry.getKey(), entry.getValue());
            }
        }

        RequestEntity<?> readRequestEntity = new RequestEntity(binaryDataToSend, requestHeaders, httpMethod, uri);

        getLogger().info("Checking Headers: " + requestHeaders.toString());
        getLogger().info(String.format("%s to API endpoint '%s': %s", httpMethod.name(), urlPath, uri));
        getLogger().info(String.format("Data Passed to %s: %s", httpMethod.name(), binaryDataToSend));

        return executeCalls(readRequestEntity, returnType, returnedInfo);
    }

    private <T> T executeCalls(RequestEntity requestEntity, Class<T> returnType, String returnedInfo) {
        try {
            if ("statusCode".equalsIgnoreCase(returnedInfo)) {
                return (T) getRestTemplate().exchange(requestEntity, returnType).getStatusCode().toString();
            }
            return getRestTemplate().exchange(requestEntity, returnType).getBody();
        } catch (HttpServerErrorException ex) {
            getLogger().debug("Error Returned From Server", ex);
            if ("statusCode".equalsIgnoreCase(returnedInfo)) {
                return (T) ex.getStatusCode().toString();
            }
            return (T) ex.getResponseBodyAsString();
        } catch (HttpClientErrorException ex1) {
            getLogger().debug("Error Returned From Client", ex1);
            if ("statusCode".equalsIgnoreCase(returnedInfo)) {
                return (T) ex1.getStatusCode().toString();
            }
            return (T) ex1.getResponseBodyAsString();
        }
    }
}
