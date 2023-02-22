package com.disney.qa.api.dgi.validationservices.multieventservice.endpointspojo.validate_with_config;

import com.disney.qa.api.dgi.DgiEndpoints;
import com.disney.qa.api.dgi.DgiParameters;
import com.disney.qa.api.dgi.validationservices.multieventservice.endpointspojo.validate.MultiEventValidate;
import com.disney.util.HARUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import net.lightbody.bmp.BrowserMobProxy;
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

public class MultiEventValidateWithConfig extends MultiEventValidate {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    SoftAssert softAssertWithConfig;

    private String configPayload;

    @JsonIgnore
    private String endpoint = String.format("%s/validate/with_config", DgiParameters.getMultiEventApiHost());


    public String getConfigPayload() {
        return configPayload;
    }

    public void setConfigPayload(String configPayload) {
        this.configPayload = configPayload;
    }

    public MultiEventValidateWithConfig() {
        super();
    }

    /**
     *
     * @deprecated  Will be deprecated, switch to
     * public MultiEventValidateWithConfig(BrowserMobProxy proxy, SoftAssert softAssert, DgiEndpoints[] endpoints, String[] events, EventSequences... eventSequences)
     *
     * @param proxy
     * @param softAssert
     * @param events
     * @param eventSequences
     */
    @Deprecated
    public MultiEventValidateWithConfig(BrowserMobProxy proxy, SoftAssert softAssert, String[] events, EventSequences... eventSequences) {
        super();
        setEvents(events);
        setProxy(proxy);
        this.configPayload = new ConfigSetup(new EntityRelationship[]{}, eventSequences).getConfigPayload();
        this.softAssertWithConfig = softAssert;
    }

    public MultiEventValidateWithConfig(BrowserMobProxy proxy, SoftAssert softAssert, DgiEndpoints[] endpoints, String[] events, EventSequences... eventSequences) {
        super();
        setEndpoints(endpoints);
        setEvents(events);
        setProxy(proxy);
        this.configPayload = new ConfigSetup(new EntityRelationship[]{}, eventSequences).getConfigPayload();
        this.softAssertWithConfig = softAssert;
    }

    public MultiEventValidateWithConfig(BrowserMobProxy proxy, SoftAssert softAssert, String[] events, EventSequences[] eventSequences, DuplicateKeys[] duplicateKeys) {
        super();
        setEvents(events);
        setProxy(proxy);
        this.configPayload = new ConfigSetup(new EntityRelationship[]{}, eventSequences, duplicateKeys).getConfigPayload();
        this.softAssertWithConfig = softAssert;
    }

    public MultiEventValidateWithConfig(BrowserMobProxy proxy, SoftAssert softAssert, DgiEndpoints[] endpoints, String[] events, Object[] entityRelationships, EventSequences[] eventSequences, DuplicateKeys[] duplicateKeys) {
        super();
        setEndpoints(endpoints);
        setEvents(events);
        setProxy(proxy);
        this.configPayload = new ConfigSetup(entityRelationships, eventSequences, duplicateKeys).getConfigPayload();
        this.softAssertWithConfig = softAssert;
    }

    /**
     *
     * @deprecated Will be deprecated, switch to
     * public MultiEventValidateWithConfig(BrowserMobProxy proxy, SoftAssert softAssert, DgiEndpoints[] endpoints, String[] events, EventSequences... eventSequences)
     *
     * @param proxy
     * @param softAssert
     * @param entityRelationships
     * @param events
     * @param eventSequences
     * @param duplicateKeys
     */
    @Deprecated
    public MultiEventValidateWithConfig(BrowserMobProxy proxy, SoftAssert softAssert, Object[] entityRelationships, String[] events, EventSequences[] eventSequences, DuplicateKeys[] duplicateKeys) {
        super();
        setEvents(events);
        setProxy(proxy);
        this.configPayload = new ConfigSetup(entityRelationships, eventSequences, duplicateKeys).getConfigPayload();
        this.softAssertWithConfig = softAssert;
    }

    @JsonIgnore
    public <T> ResponseEntity<T> queryEndpoint(HttpMethod httpMethod, String configPayload, String dataPayload) {
        HttpHeaders httpHeaders = new HttpHeaders();
        RequestEntity request = null;
        String combinedPayload = "";
        try {
            httpHeaders.add("Content-Type", "application/json");
            combinedPayload = "{\"config_payload\": " + configPayload + ", " + "\"data_payload\": " + dataPayload + "}";
            request = new RequestEntity(combinedPayload, httpHeaders, httpMethod, new URI(this.endpoint));
            LOGGER.info(String.format("HttpMethod: %s \nRequest URL: %s \nBody: \n%s",
                    httpMethod.toString(), request.getUrl(), HARUtils.beautify(combinedPayload)));
        } catch (URISyntaxException | IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        try {
            setResponse(buildRestTemplate().exchange(request, (Class<T>) String.class));
            LOGGER.info(String.format("Status: %s | Response  body: \n%s", getResponse().getStatusCode(), getResponse().getBody().toString()));
            return getResponse();
        } catch (HttpStatusCodeException e) {
            LOGGER.error(String.format("Status: %s \nResponse body: \n%s", e.getStatusCode(), e.getResponseBodyAsString()));
            this.softAssertWithConfig.fail(String.format("MultiEvent Validation Error:\nStatus: %s \nResponse body: \n%s", e.getStatusCode(), e.getResponseBodyAsString()));
        }
        return getResponse();
    }

    /**
     * Validate captured events against multi-event service
     *
     *
     * Will be deprecated, please switch to verifyPostDataEvent()
     */
    @Deprecated
    @Override
    public void verifyPostDataEvents() {
        LOGGER.info("Performing MultiEvent validation with config.\nSelected events: " + Arrays.toString(getEvents()));

        try {
            queryEndpoint(HttpMethod.POST, getConfigPayload(), collectPostData());
        }
        catch (NullPointerException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * @deprecated Method is deprecated. Please switch to verifyPostDataMultiEvent()
     * Validate captured events against multi-event service
     */
    @Deprecated
    @Override
    public void verifyPostDataEvent() {
        LOGGER.info("Performing MultiEvent validation with config.\nSelected events: " + Arrays.toString(getEvents()));

        try {
            queryEndpoint(HttpMethod.POST, getConfigPayload(), collectPostData(getEndpoints()));
        }
        catch (NullPointerException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Validate captured events against multi-event service
     */
    //TODO: Add @Override and rename once deprecated verifyPostDataEvent() is removed
    public void verifyPostDataEventVer2() {
        LOGGER.info("Performing MultiEvent validation with config.\nSelected events: " + Arrays.toString(getEvents()));

        try {
            queryEndpoint(HttpMethod.POST, getConfigPayload(), collectPostDataVer2(getEndpoints()));
        }
        catch (NullPointerException e) {
            this.softAssertWithConfig.fail("Failed to collect network traffic. Post Data is empty and cannot be checked.");
        }
    }

}
