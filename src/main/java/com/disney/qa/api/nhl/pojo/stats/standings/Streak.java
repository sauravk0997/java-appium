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
        "streakType",
        "streakNumber",
        "streakCode"
})
public class Streak {

    @JsonProperty("streakType")
    private String streakType;
    @JsonProperty("streakNumber")
    private Integer streakNumber;
    @JsonProperty("streakCode")
    private String streakCode;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public Streak() {
    }

    /**
     * @param streakNumber
     * @param streakType
     * @param streakCode
     */
    public Streak(String streakType, Integer streakNumber, String streakCode) {
        this.streakType = streakType;
        this.streakNumber = streakNumber;
        this.streakCode = streakCode;
    }

    /**
     * @return The streakType
     */
    @JsonProperty("streakType")
    public String getStreakType() {
        return streakType;
    }

    /**
     * @param streakType The streakType
     */
    @JsonProperty("streakType")
    public void setStreakType(String streakType) {
        this.streakType = streakType;
    }

    public Streak withStreakType(String streakType) {
        this.streakType = streakType;
        return this;
    }

    /**
     * @return The streakNumber
     */
    @JsonProperty("streakNumber")
    public Integer getStreakNumber() {
        return streakNumber;
    }

    /**
     * @param streakNumber The streakNumber
     */
    @JsonProperty("streakNumber")
    public void setStreakNumber(Integer streakNumber) {
        this.streakNumber = streakNumber;
    }

    public Streak withStreakNumber(Integer streakNumber) {
        this.streakNumber = streakNumber;
        return this;
    }

    /**
     * @return The streakCode
     */
    @JsonProperty("streakCode")
    public String getStreakCode() {
        return streakCode;
    }

    /**
     * @param streakCode The streakCode
     */
    @JsonProperty("streakCode")
    public void setStreakCode(String streakCode) {
        this.streakCode = streakCode;
    }

    public Streak withStreakCode(String streakCode) {
        this.streakCode = streakCode;
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

    public Streak withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(streakType).append(streakNumber).append(streakCode).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Streak) == false) {
            return false;
        }
        Streak rhs = ((Streak) other);
        return new EqualsBuilder().append(streakType, rhs.streakType).append(streakNumber, rhs.streakNumber).append(streakCode, rhs.streakCode).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
