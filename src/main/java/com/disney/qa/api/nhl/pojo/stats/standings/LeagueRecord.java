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
        "wins",
        "losses",
        "ot",
        "type"
})
public class LeagueRecord {

    @JsonProperty("wins")
    private Integer wins;
    @JsonProperty("losses")
    private Integer losses;
    @JsonProperty("ties")
    private Integer ties;
    @JsonProperty("ot")
    private Integer ot;
    @JsonProperty("type")
    private String type;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public LeagueRecord() {
    }

    /**
     * @param ot
     * @param losses
     * @param type
     * @param wins
     */
    public LeagueRecord(Integer wins, Integer losses, Integer ties, Integer ot, String type) {
        this.wins = wins;
        this.losses = losses;
        this.ties = ties;
        this.ot = ot;
        this.type = type;
    }

    /**
     * @return The wins
     */
    @JsonProperty("wins")
    public Integer getWins() {
        return wins;
    }

    /**
     * @param wins The wins
     */
    @JsonProperty("wins")
    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public LeagueRecord withWins(Integer wins) {
        this.wins = wins;
        return this;
    }

    /**
     * @return The losses
     */
    @JsonProperty("losses")
    public Integer getLosses() {
        return losses;
    }

    /**
     * @param losses The losses
     */
    @JsonProperty("losses")
    public void setLosses(Integer losses) {
        this.losses = losses;
    }

    public LeagueRecord withLosses(Integer losses) {
        this.losses = losses;
        return this;
    }

    @JsonProperty("ties")
    public Integer getTies() {
        return ties;
    }

    @JsonProperty("ties")
    public void setTies(Integer ties) {
        this.ties = ties;
    }

    /**
     * @return The ot
     */
    @JsonProperty("ot")
    public Integer getOt() {
        return ot;
    }

    /**
     * @param ot The ot
     */
    @JsonProperty("ot")
    public void setOt(Integer ot) {
        this.ot = ot;
    }

    public LeagueRecord withOt(Integer ot) {
        this.ot = ot;
        return this;
    }

    /**
     * @return The type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     * @param type The type
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    public LeagueRecord withType(String type) {
        this.type = type;
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

    public LeagueRecord withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(wins).append(losses).append(ot).append(type).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof LeagueRecord) == false) {
            return false;
        }
        LeagueRecord rhs = ((LeagueRecord) other);
        return new EqualsBuilder().append(wins, rhs.wins).append(losses, rhs.losses).append(ot, rhs.ot).append(type, rhs.type).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
