package com.disney.qa.api.nhl.pojo.stats.schedule;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.annotation.processing.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "abstractGameState",
        "codedGameState",
        "detailedState",
        "statusCode"
})
public class Status {

    @JsonProperty("abstractGameState")
    private String abstractGameState;
    @JsonProperty("codedGameState")
    private String codedGameState;
    @JsonProperty("detailedState")
    private String detailedState;
    @JsonProperty("statusCode")
    private String statusCode;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public Status() {
    }

    /**
     * @param statusCode
     * @param detailedState
     * @param codedGameState
     * @param abstractGameState
     */
    public Status(String abstractGameState, String codedGameState, String detailedState, String statusCode) {
        this.abstractGameState = abstractGameState;
        this.codedGameState = codedGameState;
        this.detailedState = detailedState;
        this.statusCode = statusCode;
    }

    /**
     * @return The abstractGameState
     */
    @JsonProperty("abstractGameState")
    public String getAbstractGameState() {
        return abstractGameState;
    }

    /**
     * @param abstractGameState The abstractGameState
     */
    @JsonProperty("abstractGameState")
    public void setAbstractGameState(String abstractGameState) {
        this.abstractGameState = abstractGameState;
    }

    public Status withAbstractGameState(String abstractGameState) {
        this.abstractGameState = abstractGameState;
        return this;
    }

    /**
     * @return The codedGameState
     */
    @JsonProperty("codedGameState")
    public String getCodedGameState() {
        return codedGameState;
    }

    /**
     * @param codedGameState The codedGameState
     */
    @JsonProperty("codedGameState")
    public void setCodedGameState(String codedGameState) {
        this.codedGameState = codedGameState;
    }

    public Status withCodedGameState(String codedGameState) {
        this.codedGameState = codedGameState;
        return this;
    }

    /**
     * @return The detailedState
     */
    @JsonProperty("detailedState")
    public String getDetailedState() {
        return detailedState;
    }

    /**
     * @param detailedState The detailedState
     */
    @JsonProperty("detailedState")
    public void setDetailedState(String detailedState) {
        this.detailedState = detailedState;
    }

    public Status withDetailedState(String detailedState) {
        this.detailedState = detailedState;
        return this;
    }

    /**
     * @return The statusCode
     */
    @JsonProperty("statusCode")
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusCode The statusCode
     */
    @JsonProperty("statusCode")
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Status withStatusCode(String statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Status withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(abstractGameState).append(codedGameState).append(detailedState).append(statusCode).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Status) == false) {
            return false;
        }
        Status rhs = ((Status) other);
        return new EqualsBuilder().append(abstractGameState, rhs.abstractGameState).append(codedGameState, rhs.codedGameState).append(detailedState, rhs.detailedState).append(statusCode, rhs.statusCode).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}