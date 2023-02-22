
package com.disney.qa.api.nhl.pojo.stats.tournaments;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.processing.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "gamePk",
    "gameNumber",
    "gameLabel",
    "necessary",
    "gameCode",
    "gameTime",
    "seriesStatus",
    "seriesStatusShort"
})
public class SeriesSummary {

    @JsonProperty("gamePk")
    private Integer gamePk;
    @JsonProperty("gameNumber")
    private Integer gameNumber;
    @JsonProperty("gameLabel")
    private String gameLabel;
    @JsonProperty("necessary")
    private Boolean necessary;
    @JsonProperty("gameCode")
    private Integer gameCode;
    @JsonProperty("gameTime")
    private String gameTime;
    @JsonProperty("seriesStatus")
    private String seriesStatus;
    @JsonProperty("seriesStatusShort")
    private String seriesStatusShort;

    /**
     * 
     * @return
     *     The gamePk
     */
    @JsonProperty("gamePk")
    public Integer getGamePk() {
        return gamePk;
    }

    /**
     * 
     * @param gamePk
     *     The gamePk
     */
    @JsonProperty("gamePk")
    public void setGamePk(Integer gamePk) {
        this.gamePk = gamePk;
    }

    /**
     * 
     * @return
     *     The gameNumber
     */
    @JsonProperty("gameNumber")
    public Integer getGameNumber() {
        return gameNumber;
    }

    /**
     * 
     * @param gameNumber
     *     The gameNumber
     */
    @JsonProperty("gameNumber")
    public void setGameNumber(Integer gameNumber) {
        this.gameNumber = gameNumber;
    }

    /**
     * 
     * @return
     *     The gameLabel
     */
    @JsonProperty("gameLabel")
    public String getGameLabel() {
        return gameLabel;
    }

    /**
     * 
     * @param gameLabel
     *     The gameLabel
     */
    @JsonProperty("gameLabel")
    public void setGameLabel(String gameLabel) {
        this.gameLabel = gameLabel;
    }

    /**
     * 
     * @return
     *     The necessary
     */
    @JsonProperty("necessary")
    public Boolean getNecessary() {
        return necessary;
    }

    /**
     * 
     * @param necessary
     *     The necessary
     */
    @JsonProperty("necessary")
    public void setNecessary(Boolean necessary) {
        this.necessary = necessary;
    }

    /**
     * 
     * @return
     *     The gameCode
     */
    @JsonProperty("gameCode")
    public Integer getGameCode() {
        return gameCode;
    }

    /**
     * 
     * @param gameCode
     *     The gameCode
     */
    @JsonProperty("gameCode")
    public void setGameCode(Integer gameCode) {
        this.gameCode = gameCode;
    }

    /**
     * 
     * @return
     *     The gameTime
     */
    @JsonProperty("gameTime")
    public String getGameTime() {
        return gameTime;
    }

    /**
     * 
     * @param gameTime
     *     The gameTime
     */
    @JsonProperty("gameTime")
    public void setGameTime(String gameTime) {
        this.gameTime = gameTime;
    }

    /**
     * 
     * @return
     *     The seriesStatus
     */
    @JsonProperty("seriesStatus")
    public String getSeriesStatus() {
        return seriesStatus;
    }

    /**
     * 
     * @param seriesStatus
     *     The seriesStatus
     */
    @JsonProperty("seriesStatus")
    public void setSeriesStatus(String seriesStatus) {
        this.seriesStatus = seriesStatus;
    }

    /**
     * 
     * @return
     *     The seriesStatusShort
     */
    @JsonProperty("seriesStatusShort")
    public String getSeriesStatusShort() {
        return seriesStatusShort;
    }

    /**
     * 
     * @param seriesStatusShort
     *     The seriesStatusShort
     */
    @JsonProperty("seriesStatusShort")
    public void setSeriesStatusShort(String seriesStatusShort) {
        this.seriesStatusShort = seriesStatusShort;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(gamePk).append(gameNumber).append(gameLabel).append(necessary).append(gameCode).append(gameTime).append(seriesStatus).append(seriesStatusShort).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SeriesSummary) == false) {
            return false;
        }
        SeriesSummary rhs = ((SeriesSummary) other);
        return new EqualsBuilder().append(gamePk, rhs.gamePk).append(gameNumber, rhs.gameNumber).append(gameLabel, rhs.gameLabel).append(necessary, rhs.necessary).append(gameCode, rhs.gameCode).append(gameTime, rhs.gameTime).append(seriesStatus, rhs.seriesStatus).append(seriesStatusShort, rhs.seriesStatusShort).isEquals();
    }

}
