package com.disney.qa.api.nhl.pojo.video.query;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.processing.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "displayName",
        "type",
        "value"
})
public class KeywordsDisplay {

    @JsonProperty("displayName")
    private String displayName;
    @JsonProperty("type")
    private String type;
    @JsonProperty("value")
    private String value;

    /**
     *
     * @return
     * The displayName
     */
    @JsonProperty("displayName")
    public String getDisplayName() {
        return displayName;
    }

    /**
     *
     * @return
     * The type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     *
     * @return
     * The value
     */
    @JsonProperty("value")
    public String getValue() {
        return value;
    }
}