package com.disney.qa.api.dgi.validationservices.sdpservice.endpointspojo.validate;

import com.disney.qa.api.dgi.DgiEndpoints;
import com.disney.qa.api.dgi.DgiParameters;
import com.disney.qa.api.dgi.validationservices.sdpservice.SdpApiProvider;
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
import java.util.Objects;

public class SdpDust extends SdpApiProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private String body = null;

    SoftAssert softAssert;

    @JsonIgnore
    private String endpoint = String.format("%s/validate/dust", DgiParameters.getSdpApiHost());

    @JsonIgnore
    private ResponseEntity response;

    @JsonIgnore
    private BrowserMobProxy proxy;

    @JsonIgnore
    private DgiEndpoints[] endpoints;

    @JsonIgnore
    private String[] events= null;


    public String getBody() {
        return body;
    }

    public ResponseEntity getResponse() {
        return response;
    }

    public BrowserMobProxy getProxy() {
        return proxy;
    }

    public DgiEndpoints[] getEndpoints() {
        return endpoints;
    }

    public String[] getEvents() {
        return events;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ResponseEntity setResponse(ResponseEntity response) {
        this.response = response;
        return this.response;
    }

    public void setProxy(BrowserMobProxy proxy) {
        this.proxy = proxy;
    }

    public void setEndpoints(DgiEndpoints[] endpoints) {
        this.endpoints = endpoints;
    }

    public void setEvents(String[] events) {
        this.events = events;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }


    public SdpDust() {
        super();
    }

    /**
     *
     * @deprecated Will be deprecated, switch to public SdpDust(BrowserMobProxy proxy, SoftAssert softAssert, DgiEndpoints[] endpoints)
     *
     * @param proxy
     * @param softAssert
     */
    @Deprecated
    public SdpDust(BrowserMobProxy proxy, SoftAssert softAssert) {
        super();
        this.proxy = proxy;
        this.softAssert = softAssert;
    }

    /**
     *
     * @deprecated Will be deprecated, switch to public SdpDust(BrowserMobProxy proxy, SoftAssert softAssert, DgiEndpoints[] endpoints, String[] events)
     *
     * @param proxy
     * @param softAssert
     * @param endpoints
     */
    @Deprecated
    public SdpDust(BrowserMobProxy proxy, SoftAssert softAssert, DgiEndpoints[] endpoints) {
        super();
        setEndpoints(endpoints);
        this.proxy = proxy;
        this.softAssert = softAssert;
    }

    public SdpDust(BrowserMobProxy proxy, SoftAssert softAssert, DgiEndpoints[] endpoints, String[] events) {
        super();
        this.proxy = proxy;
        this.softAssert = softAssert;
        setEndpoints(endpoints);
        setEvents(events);
    }

    @JsonIgnore
    public <T> ResponseEntity<T> queryEndpoint(HttpMethod httpMethod, String body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        RequestEntity<String> request = null;
        try {
            httpHeaders.add("Content-Type", "application/json");
            setBody(body);
            request = new RequestEntity(/*objectMapper.valueToTree(this)*/getBody(), httpHeaders, httpMethod, new URI(this.endpoint));
            LOGGER.info(String.format("HttpMethod: %s %nRequest URL: %s %nBody: %s", httpMethod.toString(), request.getUrl(), /*objectMapper.valueToTree(this)*/HARUtils.beautify(getBody())));
        } catch (URISyntaxException | IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        try {
            setResponse(buildRestTemplate().exchange(request, (Class<T>) String.class));
            LOGGER.info(String.format("Status: %s | Response  body: %n%s", getResponse().getStatusCode(), getResponse().getBody().toString()));
            return this.response;
        } catch (HttpStatusCodeException e) {
            setResponse(new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode()));
            LOGGER.error(String.format("Status: %s %nResponse body: %n%s", e.getStatusCode(), e.getResponseBodyAsString()));
            this.softAssert.fail(String.format("SDP Validation Error:%nStatus: %s %nResponse body: %n%s", e.getStatusCode(), e.getResponseBodyAsString()));
        }
        return this.response;
    }

    /**
     * Validate captured single events against sdp service
     * @param events
     *
     * @deprecated Will be deprecated, please switch to verifyPostDataEvent(String[] events)
     */
    @Deprecated
    public void verifyPostDataEvents(String[] events) {
        Objects.requireNonNull(events);

        LOGGER.info("Performing SDP validation for events: " + Arrays.toString(events));

        getProxy().getHar().getLog().getEntries().forEach(harEntry -> {
            if (isEntryValidDustEvent(harEntry)) {
                validateEntries(harEntry, events);
            }
        });
    }

    /**
     * Validate captured single events against sdp service
     * @deprecated Method is deprecated. Please switch to verifyPostDataEvent_ver2()
     */
    @Deprecated
    public void verifyPostDataEvent() {
        Objects.requireNonNull(events);

        LOGGER.info("Performing SDP validation for events: " + Arrays.toString(getEvents()));

        for (int currentEndpoint = 0; currentEndpoint < getEndpoints().length; currentEndpoint++) {
            for (HarEntry harEntry : getProxy().getHar().getLog().getEntries()) {
                if (isEntryValidEvent(getEndpoints()[currentEndpoint], harEntry)) {
                    validateEntries(harEntry, getEvents());
                }
            }
        }
    }


    /**
     * Validate captured single events against sdp service
     */
    //TODO: Rename after verifyPostDataEvent() is removed
    public void verifyPostDataEventVer2() {
        Objects.requireNonNull(events);

        LOGGER.info("Performing SDP validation for events: " + Arrays.toString(getEvents()));

            for (HarEntry harEntry : getProxy().getHar().getLog().getEntries()) {
                if (isEntryValidEndpoint(getEndpoints(), harEntry)) {
                    validateEntries(harEntry, getEvents());
                }
            }
    }

    private void validateEntries(HarEntry harEntry, String[] events){
        for (String postDataText : getEventObjects(harEntry)) {
            LOGGER.debug("Postdata: " + postDataText);
            /* Only iterate through events set under custom_string2 param*/
            for (int eventIndex = 0; eventIndex < events.length; eventIndex++) {
                if (postDataText.contains(events[eventIndex])) {
                    /* '[' & ']' chars removed for validation from postData */
                    setResponse(queryEndpoint(HttpMethod.POST, postDataText));
                    LOGGER.info("Event: " + events[eventIndex].substring(events[eventIndex].lastIndexOf(':') + 1) + " | Status: " + getResponse().getStatusCode());
                }
            }
        }
    }
}
