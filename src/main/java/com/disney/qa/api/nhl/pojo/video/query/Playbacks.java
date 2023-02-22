package com.disney.qa.api.nhl.pojo.video.query;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.processing.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "HTTP_CLOUD_TABLET_60"
})
public class Playbacks {

    @JsonProperty("HTTP_CLOUD_TABLET_60")
    private PlaybackScenario httpCloudTablet60;

    /**
     *
     * @return
     * The httpCloudTablet60
     */
    @JsonProperty("HTTP_CLOUD_TABLET_60")
    public PlaybackScenario getHttpCloudTablet60() {
        return httpCloudTablet60;
    }
}