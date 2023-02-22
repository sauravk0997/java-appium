package com.disney.qa.api.nhl.pojo.stats.teams;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.processing.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "id",
        "offset"
})
public class TimeZone {

    @JsonProperty("id")
    private String id;
    @JsonProperty("offset")
    private Integer offset;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public TimeZone() {
    }

    /**
     * @param id
     * @param offset
     */
    public TimeZone(String id, Integer offset) {
        this.id = id;
        this.offset = offset;
    }

    /**
     * @return The id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    public TimeZone withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * @return The offset
     */
    @JsonProperty("offset")
    public Integer getOffset() {
        return offset;
    }

    /**
     * @param offset The offset
     */
    @JsonProperty("offset")
    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public TimeZone withOffset(Integer offset) {
        this.offset = offset;
        return this;
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

    public TimeZone withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(offset).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TimeZone) == false) {
            return false;
        }
        TimeZone rhs = ((TimeZone) other);
        return new EqualsBuilder().append(id, rhs.id).append(offset, rhs.offset).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
