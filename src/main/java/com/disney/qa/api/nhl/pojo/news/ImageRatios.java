package com.disney.qa.api.nhl.pojo.news;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.processing.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "aspectRatio",
        "width",
        "height",
        "src"
})
public class ImageRatios {

    @JsonProperty("aspectRatio")
    private String aspectRatio;
    @JsonProperty("width")
    private Integer width;
    @JsonProperty("height")
    private Integer height;
    @JsonProperty("src")
    private String src;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
     * The width
     */
    @JsonProperty("width")
    public Integer getWidth() {
        return width;
    }

    /**
     *
     * @return
     * The height
     */
    @JsonProperty("height")
    public Integer getHeight() {
        return height;
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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}