package com.disney.heath_check.clients;

import com.disney.exceptions.MultiverseDevicesException;
import com.disney.heath_check.response.MultiverseDevicesResponse;
import com.disney.qa.common.http.resttemplate.RestTemplateBuilder;
import com.qaprosoft.carina.core.foundation.utils.R;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class MultiverseDevicesClient {

    private static final String MV_DEVICES_URL = R.TESTDATA.get("multiverse_devices_prod_url");

    protected final RestTemplate restTemplate = RestTemplateBuilder
            .newInstance()
            .withSpecificJsonMessageConverter()
            .withUtf8EncodingMessageConverter()
            .build();

    public MultiverseDevicesResponse requestHealthCheck(String platform, String version) {
        try {
            URI url = new URI(MV_DEVICES_URL + "/device/verify");
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url.toURL().toString())
                    .queryParam("platform", platform).queryParam("version", version);
            RequestEntity<String> request = new RequestEntity<>(HttpMethod.POST, url);
            return restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.POST,
                    request,
                    MultiverseDevicesResponse.class).getBody();
        } catch (Exception e) {
            throw new MultiverseDevicesException("Unable to request health check: " + e);
        }
    }

    public MultiverseDevicesResponse getHealthCheckStatus(String id) {
        try {
            URI url = new URI(MV_DEVICES_URL + "/device/status");
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url.toURL().toString())
                    .queryParam("id", id);
            RequestEntity<String> request = new RequestEntity<>(HttpMethod.GET, url);
            return restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    request,
                    MultiverseDevicesResponse.class).getBody();
        } catch (Exception e) {
            throw new MultiverseDevicesException("Unable to get health check status: " + e);
        }
    }
}
