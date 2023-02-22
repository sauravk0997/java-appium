package com.disney.qa.api.nhl.pojo.stats.standings;

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
        "name",
        "city",
        "timeZoneId"
})
public class Venue {

    @JsonProperty("name")
    private String name;
    @JsonProperty("city")
    private String city;
    @JsonProperty("timeZoneId")
    private String timeZoneId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public Venue() {
    }

    /**
     * @param name
     * @param timeZoneId
     * @param city
     */
    public Venue(String name, String city, String timeZoneId) {
        this.name = name;
        this.city = city;
        this.timeZoneId = timeZoneId;
    }

    /**
     * @return The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public Venue withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @return The city
     */
    @JsonProperty("city")
    public String getCity() {
        return city;
    }

    /**
     * @param city The city
     */
    @JsonProperty("city")
    public void setCity(String city) {
        this.city = city;
    }

    public Venue withCity(String city) {
        this.city = city;
        return this;
    }

    /**
     * @return The timeZoneId
     */
    @JsonProperty("timeZoneId")
    public String getTimeZoneId() {
        return timeZoneId;
    }

    /**
     * @param timeZoneId The timeZoneId
     */
    @JsonProperty("timeZoneId")
    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    public Venue withTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
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

    public Venue withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(city).append(timeZoneId).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Venue) == false) {
            return false;
        }
        Venue rhs = ((Venue) other);
        return new EqualsBuilder().append(name, rhs.name).append(city, rhs.city).append(timeZoneId, rhs.timeZoneId).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
