package com.disney.qa.api.nhl.pojo.video.query;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.processing.Generated;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "meta",
        "docs"
})
public class VideoQueryReader {

    @JsonProperty("meta")
    private Meta meta;
    @JsonProperty("docs")
    private List<Doc> docs = new ArrayList<Doc>();

    /**
     *
     * @return
     * The meta
     */
    @JsonProperty("meta")
    public Meta getMeta() {
        return meta;
    }

    /**
     *
     * @return
     * The docs
     */
    @JsonProperty("docs")
    public List<Doc> getDocs() {
        return docs;
    }
}