package com.disney.qa.api.nhl.pojo.feed.leaders;

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
        "gameType",
        "season",
        "goaltending",
        "offense"
})
public class Leaders {

    @JsonProperty("gameType")
    private String gameType;
    @JsonProperty("season")
    private String season;
    @JsonProperty("goaltending")
    private Goaltending goaltending;
    @JsonProperty("offense")
    private Offense offense;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public Leaders() {
    }

    /**
     * @param season
     * @param gameType
     * @param goaltending
     * @param offense
     */
    public Leaders(String gameType, String season, Goaltending goaltending, Offense offense) {
        this.gameType = gameType;
        this.season = season;
        this.goaltending = goaltending;
        this.offense = offense;
    }

    /**
     * @return The gameType
     */
    @JsonProperty("gameType")
    public String getGameType() {
        return gameType;
    }

    /**
     * @param gameType The gameType
     */
    @JsonProperty("gameType")
    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public Leaders withGameType(String gameType) {
        this.gameType = gameType;
        return this;
    }

    /**
     * @return The season
     */
    @JsonProperty("season")
    public String getSeason() {
        return season;
    }

    /**
     * @param season The season
     */
    @JsonProperty("season")
    public void setSeason(String season) {
        this.season = season;
    }

    public Leaders withSeason(String season) {
        this.season = season;
        return this;
    }

    /**
     * @return The goaltending
     */
    @JsonProperty("goaltending")
    public Goaltending getGoaltending() {
        return goaltending;
    }

    /**
     * @param goaltending The goaltending
     */
    @JsonProperty("goaltending")
    public void setGoaltending(Goaltending goaltending) {
        this.goaltending = goaltending;
    }

    public Leaders withGoaltending(Goaltending goaltending) {
        this.goaltending = goaltending;
        return this;
    }

    /**
     * @return The offense
     */
    @JsonProperty("offense")
    public Offense getOffense() {
        return offense;
    }

    /**
     * @param offense The offense
     */
    @JsonProperty("offense")
    public void setOffense(Offense offense) {
        this.offense = offense;
    }

    public Leaders withOffense(Offense offense) {
        this.offense = offense;
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

    public Leaders withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(gameType).append(season).append(goaltending).append(offense).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Leaders) == false) {
            return false;
        }
        Leaders rhs = ((Leaders) other);
        return new EqualsBuilder().append(gameType, rhs.gameType).append(season, rhs.season).append(goaltending, rhs.goaltending).append(offense, rhs.offense).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
