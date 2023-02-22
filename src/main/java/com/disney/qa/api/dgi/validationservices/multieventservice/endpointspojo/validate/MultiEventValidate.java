package com.disney.qa.api.dgi.validationservices.multieventservice.endpointspojo.validate;

import com.disney.qa.api.dgi.DGIBase;
import com.disney.qa.api.dgi.DgiEndpoints;
import com.disney.qa.api.dgi.DgiParameters;
import com.disney.util.HARUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.core.har.HarEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class MultiEventValidate extends DGIBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    SoftAssert softAssert;

    private String body = null;

    private DgiEndpoints[] endpoints = null;

    private String[] events= null;

    @JsonIgnore
    private String endpoint = String.format("%s/validate/", DgiParameters.getMultiEventApiHost());

    @JsonIgnore
    private ResponseEntity response;

    @JsonIgnore
    private BrowserMobProxy proxy;


    public String getBody() {
        return body;
    }

    public DgiEndpoints[] getEndpoints() {
        return endpoints;
    }

    public String[] getEvents() {
        return events;
    }

    public ResponseEntity getResponse() {
        return response;
    }

    public BrowserMobProxy getProxy() {
        return proxy;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setEndpoints(DgiEndpoints[] endpoints) {
        this.endpoints = endpoints;
    }

    public void setEvents(String[] events) {
        this.events = events;
    }

    public ResponseEntity setResponse(ResponseEntity response) {
        this.response = response;
        return this.response;
    }

    public void setProxy(BrowserMobProxy proxy) {
        this.proxy = proxy;
    }

    public MultiEventValidate() {
        super();
    }

    public MultiEventValidate(String[] events, BrowserMobProxy proxy, SoftAssert softAssert) {
        super();
        this.events = events;
        this.softAssert = softAssert;
        this.proxy = proxy;
    }

    @JsonIgnore
    public <T> ResponseEntity<T> queryEndpoint(HttpMethod httpMethod, String body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        RequestEntity request = null;
        try {
            httpHeaders.add("Content-Type", "application/json");
            request = new RequestEntity(body, httpHeaders, httpMethod, new URI(this.endpoint));
            LOGGER.info(String.format("HttpMethod: %s %nRequest URL: %s %nBody: %s",
                    httpMethod.toString(), request.getUrl(), HARUtils.beautify(body)));
        } catch (URISyntaxException | IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        try {
            setResponse(buildRestTemplate().exchange(request, (Class<T>) String.class));
            LOGGER.info(String.format("Status: %s | Response  body: %n%s", getResponse().getStatusCode(), getResponse().getBody().toString()));
            return this.response;
        } catch (HttpStatusCodeException e) {
            LOGGER.error(String.format("Status: %s %nResponse body: %n%s", e.getStatusCode(), e.getResponseBodyAsString()));
            this.softAssert.fail(String.format("MultiEvent Validation Error:%nStatus: %s %nResponse body: %n%s", e.getStatusCode(), e.getResponseBodyAsString()));
        }
        return this.response;
    }


    /**
     * Validate captured events against multi-event service
     */
    @Deprecated
    public void verifyPostDataEvents() {

        LOGGER.info("Performing MultiEvent validation.\nSelected events: " + Arrays.toString(getEvents()));

        try {
            queryEndpoint(HttpMethod.POST, collectPostData());
        }
        catch (NullPointerException e) {
                LOGGER.error(e.getMessage());
            }
    }

    /**
     * Validate captured events against multi-event service
     */
    public void verifyPostDataEvent() {

        LOGGER.info("Performing MultiEvent validation.\nSelected events: " + Arrays.toString(getEvents()));

        try {
            queryEndpoint(HttpMethod.POST, collectPostData(getEndpoints()));
        }
        catch (NullPointerException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Will be deprecated, please switch to collectPostData(DgiEndpoints... endpoint)
     */
    @Deprecated
    public String collectPostData() {
        List<String> postDataList = new LinkedList<>();
        Objects.requireNonNull(getEvents());

        getProxy().getHar().getLog().getEntries().forEach(harEntry -> {
            /**
             * Accept:
             * 1. only dust events
             * 2. single events/one server instance (not batch)
             */
            if (isEntryValidDustEvent(harEntry)) {
                for (String postDataText : getEventObjects(harEntry)) {
                    LOGGER.debug("OBJECT BODY TO TEST: " + postDataText);
                    /* Only iterate through events set under custom_string2 param*/
                    if(eventContainsDesiredType(postDataText)){
                        postDataList.add(postDataText);
                    }
                }
            }
        });

        return buildMultiEventBody(postDataList);
    }

    /**
     * @deprecated Method is deprecated. Please switch to collectPostDataVer2(DgiEndpoints... endpoint)
     */
    @Deprecated
    public String collectPostData(DgiEndpoints... endpoint) {
        List<String> postDataList = new LinkedList<>();
        Objects.requireNonNull(getEvents());

        for(int currentEndpoint = 0; currentEndpoint < endpoint.length; currentEndpoint++) {
            for(HarEntry harEntry: getProxy().getHar().getLog().getEntries())
                if (isEntryValidEvent(endpoint[currentEndpoint], harEntry)) {
                    for (String postDataText : getEventObjects(harEntry)) {
                        LOGGER.debug("OBJECT BODY TO TEST: " + postDataText);
                        /* Only iterate through events set under custom_string2 param*/
                        if (eventContainsDesiredType(postDataText)) {
                            postDataList.add(postDataText);
                        }
                    }
                }
        }

        if(postDataList.isEmpty()){
            this.softAssert.fail("Failed to collect traffic! Please check proxy settings.");
        }
        return buildMultiEventBody(postDataList);
    }

    public String collectPostDataVer2(DgiEndpoints... endpoint) {
        List<String> postDataList = new LinkedList<>();
        Objects.requireNonNull(getEvents());

        for(HarEntry harEntry: getProxy().getHar().getLog().getEntries())
            if (isEntryValidEndpoint(endpoint, harEntry)) {
                for (String postDataText : getEventObjects(harEntry)) {
                    LOGGER.debug("OBJECT BODY TO TEST: " + postDataText);
                    /* Only iterate through events set under custom_string2 param*/
                    if (eventContainsDesiredType(postDataText)) {
                        postDataList.add(postDataText);
                    }
                }
        }

        if(postDataList.isEmpty()){
            throw new NullPointerException();
        }
        return buildMultiEventBody(postDataList);
    }

    private boolean eventContainsDesiredType(String postDataText){
        try{
            for (int eventIndex = 0; eventIndex < getEvents().length; eventIndex++) {
                if (postDataText.contains(getEvents()[eventIndex])) {
                    //add post data in list to send as batch
                    return true;
                }
            }
        } catch (NullPointerException e) {
            LOGGER.debug("No postdata for current entry", e);
        }
        return false;
    }

    private String buildMultiEventBody(List<String> postDataList){
        //remove first "pageName" : "log_in__enter_password", "interaction" from validation
        try {
            if (!postDataList.isEmpty() && postDataList.get(0).contains("\"pageName\":\"log_in__enter_password\"")
                    && postDataList.get(0).contains("\"event\":\"urn:dss:event:glimpse:engagement:interaction")) {
                LOGGER.debug("Found: interaction, removing: \n" + HARUtils.beautify(postDataList.get(0)));
                postDataList.remove(0);
            }
        } catch (IOException e) {
            LOGGER.debug(e.getMessage());
        }

        /* '[' & ']' chars required in payload
         * Each postData needs to be comma separated
         * */
        return "[" + String.join(",", postDataList) + "]";
    }

}