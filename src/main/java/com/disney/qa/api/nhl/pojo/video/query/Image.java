package com.disney.qa.api.nhl.pojo.video.query;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.processing.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "cuts",
        "altText",
        "title"
})
public class Image {

    @JsonProperty("cuts")
    private Cuts cuts;
    @JsonProperty("altText")
    private String altText;
    @JsonProperty("title")
    private String title;

    /**
     *
     * @return
     * The cuts
     */
    @JsonProperty("cuts")
    public Cuts getCuts() {
        return cuts;
    }

    /**
     *
     * @return
     * The altText
     */
    @JsonProperty("altText")
    public String getAltText() {
        return altText;
    }

    /**
     *
     * @return
     * The title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }
}