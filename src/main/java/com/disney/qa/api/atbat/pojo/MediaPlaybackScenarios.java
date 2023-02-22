package com.disney.qa.api.atbat.pojo;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.annotation.processing.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "playbackScenario",
        "mediaState",
        "src"
})
public class MediaPlaybackScenarios {

    @JsonProperty("playbackScenario")
    private String playbackScenario;
    @JsonProperty("mediaState")
    private String mediaState;
    @JsonProperty("src")
    private String src;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The playbackScenario
     */
    @JsonProperty("playbackScenario")
    public String getPlaybackScenario() {
        return playbackScenario;
    }

    /**
     *
     * @param playbackScenario
     * The playbackScenario
     */
    @JsonProperty("playbackScenario")
    public void setPlaybackScenario(String playbackScenario) {
        this.playbackScenario = playbackScenario;
    }

    /**
     *
     * @return
     * The mediaState
     */
    @JsonProperty("mediaState")
    public String getMediaState() {
        return mediaState;
    }

    /**
     *
     * @param mediaState
     * The mediaState
     */
    @JsonProperty("mediaState")
    public void setMediaState(String mediaState) {
        this.mediaState = mediaState;
    }

    /**
     *
     * @return
     * The src
     */
    @JsonProperty("src")
    public String getSrc() {
        return src;
    }

    /**
     *
     * @param src
     * The src
     */
    @JsonProperty("src")
    public void setSrc(String src) {
        this.src = src;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}