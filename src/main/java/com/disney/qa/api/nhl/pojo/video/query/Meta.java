package com.disney.qa.api.nhl.pojo.video.query;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.processing.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "hits",
        "time",
        "page_size",
        "offset"
})
public class Meta {

    @JsonProperty("hits")
    private long hits;
    @JsonProperty("time")
    private long time;
    @JsonProperty("page_size")
    private long pageSize;
    @JsonProperty("offset")
    private long offset;

    /**
     *
     * @return
     * The hits
     */
    @JsonProperty("hits")
    public long getHits() {
        return hits;
    }

    /**
     *
     * @return
     * The time
     */
    @JsonProperty("time")
    public long getTime() {
        return time;
    }

    /**
     *
     * @return
     * The pageSize
     */
    @JsonProperty("page_size")
    public long getPageSize() {
        return pageSize;
    }

    /**
     *
     * @return
     * The offset
     */
    @JsonProperty("offset")
    public long getOffset() {
        return offset;
    }
}