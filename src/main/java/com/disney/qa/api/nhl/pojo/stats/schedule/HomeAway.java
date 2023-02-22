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
        "leagueRecord",
        "score",
        "team"
})
public class HomeAway {

    @JsonProperty("leagueRecord")
    private LeagueRecord leagueRecord;
    @JsonProperty("score")
    private Integer score;
    @JsonProperty("team")
    private Team team;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public HomeAway() {
    }

    /**
     * @param score
     * @param team
     */
    public HomeAway(LeagueRecord leagueRecord, Integer score, Team team) {
        this.leagueRecord = leagueRecord;
        this.score = score;
        this.team = team;
    }

    public LeagueRecord getLeagueRecord() {
        return leagueRecord;
    }

    public void setLeagueRecord(LeagueRecord leagueRecord) {
        this.leagueRecord = leagueRecord;
    }

    /**
     * @return The score
     */
    @JsonProperty("score")
    public Integer getScore() {
        return score;
    }

    /**
     * @param score The score
     */
    @JsonProperty("score")
    public void setScore(Integer score) {
        this.score = score;
    }

    public HomeAway withScore(Integer score) {
        this.score = score;
        return this;
    }

    /**
     * @return The team
     */
    @JsonProperty("team")
    public Team getTeam() {
        return team;
    }

    /**
     * @param team The team
     */
    @JsonProperty("team")
    public void setTeam(Team team) {
        this.team = team;
    }

    public HomeAway withTeam(Team team) {
        this.team = team;
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

    public HomeAway withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(leagueRecord).append(score).append(team).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof HomeAway) == false) {
            return false;
        }
        HomeAway rhs = ((HomeAway) other);
        return new EqualsBuilder().append(leagueRecord, rhs.leagueRecord).append(score, rhs.score).append(team, rhs.team).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}