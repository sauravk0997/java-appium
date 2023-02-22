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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "standingsType",
        "league",
        "division",
        "conference",
        "teamRecords"
})
public class Record {

    @JsonProperty("standingsType")
    private String standingsType;
    @JsonProperty("league")
    private League league;
    @JsonProperty("division")
    private Division division;
    @JsonProperty("conference")
    private Conference conference;
    @JsonProperty("teamRecords")
    private List<TeamRecord> teamRecords = new ArrayList<TeamRecord>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public Record() {
    }

    /**
     * @param division
     * @param standingsType
     * @param teamRecords
     * @param conference
     * @param league
     */
    public Record(String standingsType, League league, Division division, Conference conference, List<TeamRecord> teamRecords) {
        this.standingsType = standingsType;
        this.league = league;
        this.division = division;
        this.conference = conference;
        this.teamRecords = teamRecords;
    }

    /**
     * @return The standingsType
     */
    @JsonProperty("standingsType")
    public String getStandingsType() {
        return standingsType;
    }

    /**
     * @param standingsType The standingsType
     */
    @JsonProperty("standingsType")
    public void setStandingsType(String standingsType) {
        this.standingsType = standingsType;
    }

    public Record withStandingsType(String standingsType) {
        this.standingsType = standingsType;
        return this;
    }

    /**
     * @return The league
     */
    @JsonProperty("league")
    public League getLeague() {
        return league;
    }

    /**
     * @param league The league
     */
    @JsonProperty("league")
    public void setLeague(League league) {
        this.league = league;
    }

    public Record withLeague(League league) {
        this.league = league;
        return this;
    }

    /**
     * @return The division
     */
    @JsonProperty("division")
    public Division getDivision() {
        return division;
    }

    /**
     * @param division The division
     */
    @JsonProperty("division")
    public void setDivision(Division division) {
        this.division = division;
    }

    public Record withDivision(Division division) {
        this.division = division;
        return this;
    }

    /**
     * @return The conference
     */
    @JsonProperty("conference")
    public Conference getConference() {
        return conference;
    }

    /**
     * @param conference The conference
     */
    @JsonProperty("conference")
    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public Record withConference(Conference conference) {
        this.conference = conference;
        return this;
    }

    /**
     * @return The teamRecords
     */
    @JsonProperty("teamRecords")
    public List<TeamRecord> getTeamRecords() {
        return teamRecords;
    }

    /**
     * @param teamRecords The teamRecords
     */
    @JsonProperty("teamRecords")
    public void setTeamRecords(List<TeamRecord> teamRecords) {
        this.teamRecords = teamRecords;
    }

    public Record withTeamRecords(List<TeamRecord> teamRecords) {
        this.teamRecords = teamRecords;
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

    public Record withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(standingsType).append(league).append(division).append(conference).append(teamRecords).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Record) == false) {
            return false;
        }
        Record rhs = ((Record) other);
        return new EqualsBuilder().append(standingsType, rhs.standingsType).append(league, rhs.league).append(division, rhs.division).append(conference, rhs.conference).append(teamRecords, rhs.teamRecords).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
