package com.disney.qa.tests.dgi.api;

import com.disney.qa.api.dgi.validationservices.sdpservice.endpointspojo.validate.SdpEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.invoke.MethodHandles;

@Deprecated
public class SdpApiTest extends SdpBaseApiTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Test(enabled = false)
    public void queryEventEndpoint() {
        SdpEvent event = new SdpEvent();
        event.setType("urn:dss:event:fed:media:playback:requested");

        response = event.queryEndpoint(HttpMethod.POST, "xxx", "yyy", "userAction",
                "zzz", true, false);

        LOGGER.info("Expected Response: 2xx Successful" + "\n" + event.getExpectedResponse());
        Assert.assertTrue(response.getStatusCode().is2xxSuccessful(), "Expected - Endpoint to return 2xx");
        Assert.assertTrue(response.getBody().equals(event.getExpectedResponse()),
                String.format("Query did not return expected response body \nExpected: %s \nActual: %s",
                        event.getExpectedResponse(), response.getBody()));
    }
}