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
        "team",
        "goalsAgainst",
        "goalsScored",
        "points",
        "divisionRank",
        "conferenceRank",
        "leagueRank",
        "wildCardRank",
        "row",
        "gamesPlayed",
        "streak",
        "leagueRecord",
        "record"
})
public class TeamRecord {

    @JsonProperty("team")
    private Team team;
    @JsonProperty("goalsAgainst")
    private Integer goalsAgainst;
    @JsonProperty("goalsScored")
    private Integer goalsScored;
    @JsonProperty("points")
    private Integer points;
    @JsonProperty("divisionRank")
    private String divisionRank;
    @JsonProperty("conferenceRank")
    private String conferenceRank;
    @JsonProperty("leagueRank")
    private String leagueRank;
    @JsonProperty("wildCardRank")
    private String wildCardRank;
    @JsonProperty("row")
    private Integer row;
    @JsonProperty("gamesPlayed")
    private Integer gamesPlayed;
    @JsonProperty("streak")
    private Streak streak;
    @JsonProperty("leagueRecord")
    private LeagueRecord leagueRecord;
    @JsonProperty("records")
    private Records records;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     */
    public TeamRecord() {
    }

    /**
     * @param streak
     * @param conferenceRank
     * @param goalsScored
     * @param gamesPlayed
     * @param leagueRank
     * @param divisionRank
     * @param wildCardRank
     * @param points
     * @param goalsAgainst
     * @param team
     * @param row
     * @param leagueRecord
     */
    public TeamRecord(Team team, Integer goalsAgainst, Integer goalsScored, Integer points, String divisionRank, String conferenceRank, String leagueRank, String wildCardRank, Integer row, Integer gamesPlayed, Streak streak, LeagueRecord leagueRecord) {
        this.team = team;
        this.goalsAgainst = goalsAgainst;
        this.goalsScored = goalsScored;
        this.points = points;
        this.divisionRank = divisionRank;
        this.conferenceRank = conferenceRank;
        this.leagueRank = leagueRank;
        this.wildCardRank = wildCardRank;
        this.row = row;
        this.gamesPlayed = gamesPlayed;
        this.streak = streak;
        this.leagueRecord = leagueRecord;
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

    public TeamRecord withTeam(Team team) {
        this.team = team;
        return this;
    }

    /**
     * @return The goalsAgainst
     */
    @JsonProperty("goalsAgainst")
    public Integer getGoalsAgainst() {
        return goalsAgainst;
    }

    /**
     * @param goalsAgainst The goalsAgainst
     */
    @JsonProperty("goalsAgainst")
    public void setGoalsAgainst(Integer goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }

    public TeamRecord withGoalsAgainst(Integer goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
        return this;
    }

    /**
     * @return The goalsScored
     */
    @JsonProperty("goalsScored")
    public Integer getGoalsScored() {
        return goalsScored;
    }

    /**
     * @param goalsScored The goalsScored
     */
    @JsonProperty("goalsScored")
    public void setGoalsScored(Integer goalsScored) {
        this.goalsScored = goalsScored;
    }

    public TeamRecord withGoalsScored(Integer goalsScored) {
        this.goalsScored = goalsScored;
        return this;
    }

    /**
     * @return The points
     */
    @JsonProperty("points")
    public Integer getPoints() {
        return points;
    }

    /**
     * @param points The points
     */
    @JsonProperty("points")
    public void setPoints(Integer points) {
        this.points = points;
    }

    public TeamRecord withPoints(Integer points) {
        this.points = points;
        return this;
    }

    /**
     * @return The divisionRank
     */
    @JsonProperty("divisionRank")
    public String getDivisionRank() {
        return divisionRank;
    }

    /**
     * @param divisionRank The divisionRank
     */
    @JsonProperty("divisionRank")
    public void setDivisionRank(String divisionRank) {
        this.divisionRank = divisionRank;
    }

    public TeamRecord withDivisionRank(String divisionRank) {
        this.divisionRank = divisionRank;
        return this;
    }

    /**
     * @return The conferenceRank
     */
    @JsonProperty("conferenceRank")
    public String getConferenceRank() {
        return conferenceRank;
    }

    /**
     * @param conferenceRank The conferenceRank
     */
    @JsonProperty("conferenceRank")
    public void setConferenceRank(String conferenceRank) {
        this.conferenceRank = conferenceRank;
    }

    public TeamRecord withConferenceRank(String conferenceRank) {
        this.conferenceRank = conferenceRank;
        return this;
    }

    /**
     * @return The leagueRank
     */
    @JsonProperty("leagueRank")
    public String getLeagueRank() {
        return leagueRank;
    }

    /**
     * @param leagueRank The leagueRank
     */
    @JsonProperty("leagueRank")
    public void setLeagueRank(String leagueRank) {
        this.leagueRank = leagueRank;
    }

    public TeamRecord withLeagueRank(String leagueRank) {
        this.leagueRank = leagueRank;
        return this;
    }

    /**
     * @return The wildCardRank
     */
    @JsonProperty("wildCardRank")
    public String getWildCardRank() {
        return wildCardRank;
    }

    /**
     * @param wildCardRank The wildCardRank
     */
    @JsonProperty("wildCardRank")
    public void setWildCardRank(String wildCardRank) {
        this.wildCardRank = wildCardRank;
    }

    public TeamRecord withWildCardRank(String wildCardRank) {
        this.wildCardRank = wildCardRank;
        return this;
    }

    /**
     * @return The row
     */
    @JsonProperty("row")
    public Integer getRow() {
        return row;
    }

    /**
     * @param row The row
     */
    @JsonProperty("row")
    public void setRow(Integer row) {
        this.row = row;
    }

    public TeamRecord withRow(Integer row) {
        this.row = row;
        return this;
    }

    /**
     * @return The gamesPlayed
     */
    @JsonProperty("gamesPlayed")
    public Integer getGamesPlayed() {
        return gamesPlayed;
    }

    /**
     * @param gamesPlayed The gamesPlayed
     */
    @JsonProperty("gamesPlayed")
    public void setGamesPlayed(Integer gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public TeamRecord withGamesPlayed(Integer gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
        return this;
    }

    /**
     * @return The streak
     */
    @JsonProperty("streak")
    public Streak getStreak() {
        return streak;
    }

    /**
     * @param streak The streak
     */
    @JsonProperty("streak")
    public void setStreak(Streak streak) {
        this.streak = streak;
    }

    public TeamRecord withStreak(Streak streak) {
        this.streak = streak;
        return this;
    }

    /**
     * @return The leagueRecord
     */
    @JsonProperty("leagueRecord")
    public LeagueRecord getLeagueRecord() {
        return leagueRecord;
    }

    /**
     * @param leagueRecord The leagueRecord
     */
    @JsonProperty("leagueRecord")
    public void setLeagueRecord(LeagueRecord leagueRecord) {
        this.leagueRecord = leagueRecord;
    }

    public TeamRecord withLeagueRecord(LeagueRecord leagueRecord) {
        this.leagueRecord = leagueRecord;
        return this;
    }

    @JsonProperty("records")
    public Records getRecords() {
        return records;
    }

    @JsonProperty("records")
    public void setRecords(Records records) {
        this.records = records;
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

    public TeamRecord withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(team).append(goalsAgainst).append(goalsScored).append(points).append(divisionRank).append(conferenceRank).append(leagueRank).append(wildCardRank).append(row).append(gamesPlayed).append(streak).append(leagueRecord).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TeamRecord) == false) {
            return false;
        }
        TeamRecord rhs = ((TeamRecord) other);
        return new EqualsBuilder().append(team, rhs.team).append(goalsAgainst, rhs.goalsAgainst).append(goalsScored, rhs.goalsScored).append(points, rhs.points).append(divisionRank, rhs.divisionRank).append(conferenceRank, rhs.conferenceRank).append(leagueRank, rhs.leagueRank).append(wildCardRank, rhs.wildCardRank).append(row, rhs.row).append(gamesPlayed, rhs.gamesPlayed).append(streak, rhs.streak).append(leagueRecord, rhs.leagueRecord).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
