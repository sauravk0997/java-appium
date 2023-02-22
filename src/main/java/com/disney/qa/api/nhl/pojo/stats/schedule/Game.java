
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
        "gamePk",
        "link",
        "gameType",
        "season",
        "gameDate",
        "status",
        "teams",
        "linescore",
        "venue",
        "content"
})
public class Game {

    @JsonProperty("gamePk")
    private Integer gamePk;
    @JsonProperty("link")
    private String link;
    @JsonProperty("gameType")
    private String gameType;
    @JsonProperty("season")
    private String season;
    @JsonProperty("gameDate")
    private String gameDate;
    @JsonProperty("status")
    private Status status;
    @JsonProperty("teams")
    private Teams teams;
    @JsonProperty("venue")
    private Venue venue;
    @JsonProperty("content")
    private Content content;
    @JsonProperty("linescore")
    private Linescore linescore;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The gamePk
     */
    @JsonProperty("gamePk")
    public Integer getGamePk() {
        return gamePk;
    }

    /**
     * @param gamePk The gamePk
     */
    @JsonProperty("gamePk")
    public void setGamePk(Integer gamePk) {
        this.gamePk = gamePk;
    }

    public Game withGamePk(Integer gamePk) {
        this.gamePk = gamePk;
        return this;
    }

    /**
     * @return The link
     */
    @JsonProperty("link")
    public String getLink() {
        return link;
    }

    /**
     * @param link The link
     */
    @JsonProperty("link")
    public void setLink(String link) {
        this.link = link;
    }

    public Game withLink(String link) {
        this.link = link;
        return this;
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

    public Game withGameType(String gameType) {
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

    public Game withSeason(String season) {
        this.season = season;
        return this;
    }

    /**
     * @return The gameDate
     */
    @JsonProperty("gameDate")
    public String getGameDate() {
        return gameDate;
    }

    /**
     * @param gameDate The gameDate
     */
    @JsonProperty("gameDate")
    public void setGameDate(String gameDate) {
        this.gameDate = gameDate;
    }

    public Game withGameDate(String gameDate) {
        this.gameDate = gameDate;
        return this;
    }

    /**
     * @return The status
     */
    @JsonProperty("status")
    public Status getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    @JsonProperty("status")
    public void setStatus(Status status) {
        this.status = status;
    }

    public Game withStatus(Status status) {
        this.status = status;
        return this;
    }

    /**
     * @return The teams
     */
    @JsonProperty("teams")
    public Teams getTeams() {
        return teams;
    }

    /**
     * @param teams The teams
     */
    @JsonProperty("teams")
    public void setTeams(Teams teams) {
        this.teams = teams;
    }

    public Game withTeams(Teams teams) {
        this.teams = teams;
        return this;
    }

    /**
     * @return The venue
     */
    @JsonProperty("venue")
    public Venue getVenue() {
        return venue;
    }

    /**
     * @param venue The venue
     */
    @JsonProperty("venue")
    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public Game withVenue(Venue venue) {
        this.venue = venue;
        return this;
    }

    /**
     * @return The content
     */
    @JsonProperty("content")
    public Content getContent() {
        return content;
    }

    /**
     * @param content The content
     */
    @JsonProperty("content")
    public void setContent(Content content) {
        this.content = content;
    }

    public Game withContent(Content content) {
        this.content = content;
        return this;
    }

    @JsonProperty("linescore")
    public Linescore getLinescore() {
        return linescore;
    }

    @JsonProperty("linescore")
    public void setLinescore(Linescore linescore) {
        this.linescore = linescore;
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

    public Game withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(gamePk).append(link).append(gameType).append(season).append(gameDate).append(status).append(teams).append(venue).append(content).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Game) == false) {
            return false;
        }
        Game rhs = ((Game) other);
        return new EqualsBuilder().append(gamePk, rhs.gamePk).append(link, rhs.link).append(gameType, rhs.gameType).append(season, rhs.season).append(gameDate, rhs.gameDate).append(status, rhs.status).append(teams, rhs.teams).append(venue, rhs.venue).append(content, rhs.content).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
