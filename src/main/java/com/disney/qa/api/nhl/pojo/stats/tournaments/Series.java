
package com.disney.qa.api.nhl.pojo.stats.tournaments;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.processing.Generated;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "seriesNumber",
    "seriesCode",
    "names",
    "currentGame",
    "round",
    "matchupTeams"
})
public class Series {

    @JsonProperty("seriesNumber")
    private Integer seriesNumber;
    @JsonProperty("seriesCode")
    private String seriesCode;
    @JsonProperty("names")
    private Names_ names;
    @JsonProperty("currentGame")
    private CurrentGame currentGame;
    @JsonProperty("round")
    private Round_ round;
    @JsonProperty("matchupTeams")
    private List<MatchupTeam> matchupTeams = new ArrayList<MatchupTeam>();

    /**
     * 
     * @return
     *     The seriesNumber
     */
    @JsonProperty("seriesNumber")
    public Integer getSeriesNumber() {
        return seriesNumber;
    }

    /**
     * 
     * @param seriesNumber
     *     The seriesNumber
     */
    @JsonProperty("seriesNumber")
    public void setSeriesNumber(Integer seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    /**
     * 
     * @return
     *     The seriesCode
     */
    @JsonProperty("seriesCode")
    public String getSeriesCode() {
        return seriesCode;
    }

    /**
     * 
     * @param seriesCode
     *     The seriesCode
     */
    @JsonProperty("seriesCode")
    public void setSeriesCode(String seriesCode) {
        this.seriesCode = seriesCode;
    }

    /**
     * 
     * @return
     *     The names
     */
    @JsonProperty("names")
    public Names_ getNames() {
        return names;
    }

    /**
     * 
     * @param names
     *     The names
     */
    @JsonProperty("names")
    public void setNames(Names_ names) {
        this.names = names;
    }

    /**
     * 
     * @return
     *     The currentGame
     */
    @JsonProperty("currentGame")
    public CurrentGame getCurrentGame() {
        return currentGame;
    }

    /**
     * 
     * @param currentGame
     *     The currentGame
     */
    @JsonProperty("currentGame")
    public void setCurrentGame(CurrentGame currentGame) {
        this.currentGame = currentGame;
    }

    /**
     * 
     * @return
     *     The round
     */
    @JsonProperty("round")
    public Round_ getRound() {
        return round;
    }

    /**
     * 
     * @param round
     *     The round
     */
    @JsonProperty("round")
    public void setRound(Round_ round) {
        this.round = round;
    }

    /**
     * 
     * @return
     *     The matchupTeams
     */
    @JsonProperty("matchupTeams")
    public List<MatchupTeam> getMatchupTeams() {
        return matchupTeams;
    }

    /**
     * 
     * @param matchupTeams
     *     The matchupTeams
     */
    @JsonProperty("matchupTeams")
    public void setMatchupTeams(List<MatchupTeam> matchupTeams) {
        this.matchupTeams = matchupTeams;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(seriesNumber).append(seriesCode).append(names).append(currentGame).append(round).append(matchupTeams).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Series) == false) {
            return false;
        }
        Series rhs = ((Series) other);
        return new EqualsBuilder().append(seriesNumber, rhs.seriesNumber).append(seriesCode, rhs.seriesCode).append(names, rhs.names).append(currentGame, rhs.currentGame).append(round, rhs.round).append(matchupTeams, rhs.matchupTeams).isEquals();
    }

}
