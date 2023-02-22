package com.disney.qa.api.dgi.validationservices.sdpservice.endpointspojo.validate;

import com.disney.qa.api.dgi.DgiParameters;
import com.disney.qa.api.dgi.validationservices.sdpservice.SdpApiProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.URISyntaxException;

public class SdpEvent extends SdpApiProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private String contentId = null;

    private String mediaId = null;

    private String playbackIntent = null;

    private String playbackSessionId = null;

    @JsonIgnore
    private String type = null;

    private boolean preBuffer = true;

    private boolean offline = false;

    @JsonIgnore
    private static final String EXPECTED_RESPONSE =
            "{\"message\":\"Successfully validated Fed JSON message\",\"_type\":\"com.disneystreaming.sdp.http.Success\"}";

    @JsonIgnore
    private String endpoint = String.format("%s/validate/event", DgiParameters.getSdpApiHost());



    public String getContentId() {
        return contentId;
    }

    public String getMediaId() {
        return mediaId;
    }

    public String getPlaybackIntent() {
        return playbackIntent;
    }

    public String getPlaybackSessionId() {
        return playbackSessionId;
    }

    public boolean isPreBuffer() {
        return preBuffer;
    }

    public boolean isOffline() {
        return offline;
    }

    public static String getExpectedResponse() {
        return EXPECTED_RESPONSE;
    }

    @JsonIgnore
    public String getType() {
        return type;
    }


    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public void setPlaybackIntent(String playbackIntent) {
        this.playbackIntent = playbackIntent;
    }

    public void setPlaybackSessionId(String playbackSessionId) {
        this.playbackSessionId = playbackSessionId;
    }

    public void setPreBuffer(boolean preBuffer) {
        this.preBuffer = preBuffer;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonIgnore
    public <T> ResponseEntity<T> queryEndpoint(HttpMethod httpMethod, String contentId, String mediaId, String playbackIntent, String playbackSessionId, boolean preBuffer, boolean offline) {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders httpHeaders = new HttpHeaders();
        RequestEntity request = null;
        try {
            if(getType() != null && !getType().isEmpty()) {
                this.endpoint = endpoint.concat("?type=" + getType());
            }
            httpHeaders.add("Content-Type", "application/json");
            this.contentId = contentId;
            this.mediaId = mediaId;
            this.playbackIntent = playbackIntent;
            this.playbackSessionId = playbackSessionId;
            this.preBuffer = preBuffer;
            this.offline = offline;
            request = new RequestEntity(objectMapper.valueToTree(this), httpHeaders, httpMethod, new URI(this.endpoint));
            LOGGER.info(String.format("HttpMethod: %s \nRequest URL: %s \nPayload: %s", httpMethod.toString(), request.getUrl(), objectMapper.valueToTree(this)));
        } catch (URISyntaxException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return buildRestTemplate().exchange(request, (Class<T>) String.class);
    }

}
