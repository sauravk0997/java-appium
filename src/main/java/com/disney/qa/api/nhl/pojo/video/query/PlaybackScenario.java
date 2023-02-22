package com.disney.qa.api.nhl.pojo.video.query;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.processing.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "url",
        "width",
        "name",
        "height"
})
public class PlaybackScenario {

    @JsonProperty("url")
    private String url;
    @JsonProperty("width")
    private Object width;
    @JsonProperty("name")
    private String name;
    @JsonProperty("height")
    private Object height;

    /**
     *
     * @return
     * The url
     */
    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    /**
     *
     * @return
     * The width
     */
    @JsonProperty("width")
    public Object getWidth() {
        return width;
    }

    /**
     *
     * @return
     * The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     * The height
     */
    @JsonProperty("height")
    public Object getHeight() {
        return height;
    }
}