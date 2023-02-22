package com.disney.qa.api.nhl.pojo.video.query;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.processing.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "src",
        "height",
        "width",
        "at2x",
        "aspectRatio",
        "at3x"
})
public class Aspects {

    @JsonProperty("src")
    private String src;
    @JsonProperty("height")
    private long height;
    @JsonProperty("width")
    private long width;
    @JsonProperty("at2x")
    private String at2x;
    @JsonProperty("aspectRatio")
    private String aspectRatio;
    @JsonProperty("at3x")
    private String at3x;

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
     * @return
     * The height
     */
    @JsonProperty("height")
    public long getHeight() {
        return height;
    }

    /**
     *
     * @return
     * The width
     */
    @JsonProperty("width")
    public long getWidth() {
        return width;
    }

    /**
     *
     * @return
     * The at2x
     */
    @JsonProperty("at2x")
    public String getAt2x() {
        return at2x;
    }

    /**
     *
     * @return
     * The aspectRatio
     */
    @JsonProperty("aspectRatio")
    public String getAspectRatio() {
        return aspectRatio;
    }

    /**
     *
     * @return
     * The at3x
     */
    @JsonProperty("at3x")
    public String getAt3x() {
        return at3x;
    }
}