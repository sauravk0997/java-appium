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
        "away",
        "home"
})
public class Teams {

    // TODO merge Home and Away classes
    @JsonProperty("away")
    private HomeAway away;
    @JsonProperty("home")
    private HomeAway home;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public Teams() {
    }

    /**
     * @param home
     * @param away
     */
    public Teams(HomeAway away, HomeAway home) {
        this.away = away;
        this.home = home;
    }

    /**
     * @return The away
     */
    @JsonProperty("away")
    public HomeAway getAway() {
        return away;
    }

    /**
     * @param away The away
     */
    @JsonProperty("away")
    public void setAway(HomeAway away) {
        this.away = away;
    }

    public Teams withAway(HomeAway away) {
        this.away = away;
        return this;
    }

    /**
     * @return The home
     */
    @JsonProperty("home")
    public HomeAway getHome() {
        return home;
    }

    /**
     * @param home The home
     */
    @JsonProperty("home")
    public void setHome(HomeAway home) {
        this.home = home;
    }

    public Teams withHome(HomeAway home) {
        this.home = home;
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

    public Teams withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(away).append(home).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Teams) == false) {
            return false;
        }
        Teams rhs = ((Teams) other);
        return new EqualsBuilder().append(away, rhs.away).append(home, rhs.home).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}